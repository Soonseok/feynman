package dev.soon.feynman.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.service.DailyStatsService;
import dev.soon.feynman.arpltnStatsSvc.service.MonthlyStatsService;
import dev.soon.feynman.arpltnStatsSvc.service.SggStatsService;
import dev.soon.feynman.arpltnStatsSvc.service.SidoStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArpltnDataScheduler {

    private final ObjectMapper objectMapper;
    private final DailyStatsService dailyStatsService;
    private final MonthlyStatsService monthlyStatsService;
    private final SggStatsService sggStatsService;
    private final SidoStatsService sidoStatsService;
    private final ArpltnStatsDao arpltnStatsDao;

    // 시도 목록을 메모리에 고정 (17개)
    private static final List<String> SIDO_NAMES = List.of(
            "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원",
            "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종");

    // 측정 항목 목록을 메모리에 고정 (6개)
    private static final List<String> ITEM_CODES = List.of(
            "SO2", "CO", "O3", "NO2", "PM10", "PM25");

    /**
     * 1. 측정소별 일평균 정보 조회 (2개 조로 나눠서 매일 번갈아 가며 실행)
     * Cron: "0 0 2 * * *" (매일 새벽 2시)
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void scheduleDailyStatsMsrstn() {
        log.info("Starting scheduled daily station stats job.");
        String fileName;

        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        if (dayOfWeek % 2 != 0) {
            fileName = "/static/api_files/arpltn/stationName1.json";
        } else {
            fileName = "/static/api_files/arpltn/stationName2.json";
        }
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            if (is == null) {
                log.error("File not found: {}", fileName);
                return;
            }

            List<String> stationNames = objectMapper.readValue(is, new TypeReference<>() {});

            for (String station : stationNames) {
                // 1. DB에서 해당 측정소의 마지막 데이터 날짜를 가져옵니다.
                LocalDateTime lastDate = arpltnStatsDao.findLastMeasurementDate(station);
                LocalDate start = (lastDate != null) ? lastDate.toLocalDate().plusDays(1) : LocalDate.now().minusMonths(1); // 데이터가 없다면 한 달 전부터 시작
                LocalDate end = LocalDate.now().minusDays(1);

                if (start.isAfter(end)) {
                    log.info("Station {} is already up to date.", station);
                    continue;
                }

                // 2. 누락된 날짜 범위를 계산하여 데이터를 가져옵니다.
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                String startDate = start.format(formatter);
                String endDate = end.format(formatter);

                log.info("Fetching data for {} from {} to {}", station, startDate, endDate);

                // 3. API 호출
                dailyStatsService.getDailyStats(station, startDate, endDate);

                // API 요청 지연
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("API call sleep was interrupted", e);
                }
            }
            log.info("Daily stats job finished. Processed {} stations from file: {}", stationNames.size(), fileName);
        } catch (IOException e) {
            log.error("Failed to read station list file: {}", fileName, e);
        }
    }

    /**
     * 2. 측정소별 월평균 정보 조회 (매월 1, 2일에 나눠서 실행)
     * Cron: "0 45 4 1,2 * *" (매월 1, 2일 새벽 4시 45분)
     */
    @Scheduled(cron = "0 45 4 1,2 * *")
    public void scheduleMonthlyStatsMsrstn() {
        log.info("Starting scheduled monthly station stats job.");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth thisMonth = YearMonth.now();               // 이번 달
        YearMonth lastMonth = thisMonth.minusMonths(1);
        String yy = formatter.format(lastMonth);

        String fileName;
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        if (dayOfMonth == 1) {
            fileName = "/static/api_files/arpltn/stationName1.json";
        } else {
            fileName = "/static/api_files/arpltn/stationName2.json";
        }
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            List<String> stationNames = objectMapper.readValue(is, new TypeReference<>() {});
            for (String station : stationNames) {
                monthlyStatsService.getMonthlyStats(station, yy, yy);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("API call sleep was interrupted", e);
                }
            }
            log.info("Monthly station stats job finished for file: {}. Processed {} stations.", fileName, stationNames.size());
        } catch (IOException e) {
            log.error("Failed to read station list file: {}", fileName, e);
        }
    }

    /**
     * 3. 시도별 시간평균 정보 조회 (매시간 15분에 이전 시간 데이터 조회)
     * Cron: "0 15 * * * *"
     */
    @Scheduled(cron = "0 15 * * * *")
    public void scheduleHourlySidoStats() {
        log.info("Starting scheduled hourly sido stats job.");
        for (String itemCode : ITEM_CODES) {
            sidoStatsService.getSidoStats(itemCode, "HOUR", "WEEK");
            try {
                    TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("API call sleep was interrupted", e);
            }
        }
        log.info("Hourly sido stats job finished.");
    }

    /**
     * 4. 시도별 일평균 정보 조회 (매일 새벽 3시 30분)
     * Cron: "0 30 3 * * *"
     */
    @Scheduled(cron = "0 30 3 * * *")
    public void scheduleDailySidoStats() {
        log.info("Starting scheduled daily sido stats job.");
        for (String itemCode : ITEM_CODES) {
            sidoStatsService.getSidoStats(itemCode, "DAILY", "WEEK");
            try {
                    TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("API call sleep was interrupted", e);
            }
        }
        log.info("Daily sido stats job finished.");
    }

    /**
     * 5. 시군구별 일평균 정보 조회 (매일 새벽 3시 45분)
     * Cron: "0 45 3 * * *"
     */
    @Scheduled(cron = "0 45 3 * * *")
    public void scheduleDailySggStats() {
        log.info("Starting scheduled daily sgg stats job.");
        for (String sidoName : SIDO_NAMES) {
            sggStatsService.getSggStats(sidoName, "DAILY");
            try {
                    TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("API call sleep was interrupted", e);
            }
        }
        log.info("Daily sgg stats job finished.");
    }

    /**
     * station_code 매치 스케줄러
     */
    @Scheduled(cron = "0 20 * * * *")
    @Transactional
    public void scheduleNewSidoStatsJob() {
        log.info("Starting station code match method.");
        try {
            arpltnStatsDao.disableSafeUpdates(); // 안전 모드 비활성화
            arpltnStatsDao.matchStationCode();  // 업데이트 쿼리 실행
        } catch (Exception e) {
            log.error("Station code matcher got a problem :", e);
            throw e;
        }
        log.info("End station code match method.");
    }
}
