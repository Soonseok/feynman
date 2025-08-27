//package dev.soon.feynman.scheduler;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dev.soon.feynman.arpltnStatsSvc.service.DailyStatsService;
//import dev.soon.feynman.arpltnStatsSvc.service.MonthlyStatsService;
//import dev.soon.feynman.arpltnStatsSvc.service.SggStatsService;
//import dev.soon.feynman.arpltnStatsSvc.service.SidoStatsService;
//import dev.soon.feynman.config.MsrstnService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ArpltnDataScheduler {
//
//    private final ObjectMapper objectMapper;
//    private final MsrstnService msrstnService;
//    private final DailyStatsService dailyStatsService;
//    private final MonthlyStatsService monthlyStatsService;
//    private final SggStatsService sggStatsService;
//    private final SidoStatsService sidoStatsService;
//
//    // 시도 목록을 메모리에 고정 (17개)
//    private static final List<String> SIDO_NAMES = List.of(
//            "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원",
//            "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종");
//
//    // 측정 항목 목록을 메모리에 고정 (6개)
//    private static final List<String> ITEM_CODES = List.of(
//            "SO2", "CO", "O3", "NO2", "PM10", "PM2.5");
//
//    @Scheduled(cron = "0 0 2 * * *")
//    public void scheduleDailyStatsMsrstn() {
//        log.info("Starting scheduled daily station stats job.");
//        String fileName;
//
//        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();      // 1=월, 2=화, ..., 7=일
//        long  daysToFetch = 1;
//        if (dayOfWeek == 2) {
//            daysToFetch = 2;
//        }
//        LocalDate endDateLD = LocalDate.now().minusDays(1);
//        LocalDate startDateLD = endDateLD.minusDays(daysToFetch - 1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String endDate = endDateLD.format(formatter);
//        String startDate = startDateLD.format(formatter);
//
//        // 요일이 홀수(월,수,금,일)이면 1조, 짝수(화,목,토)이면 2조
//        if (dayOfWeek % 2 != 0) {
//            fileName = "/static/stationName1.json";
//        } else {
//            fileName = "/static/stationName2.json";
//        }
//
//        try (InputStream is = getClass().getResourceAsStream(fileName)) {
//            List<String> stationNames = objectMapper.readValue(is, new TypeReference<>() {});
//            for (String station : stationNames) {
//                dailyStatsService.getDailyStats(station, startDate, endDate);
//            }
//            log.info("Daily station stats job finished. Processed {} stations from file: {}", stationNames.size(), fileName);
//        } catch (IOException e) {
//            log.error("Failed to read station list file: {}", fileName, e);
//        }
//    }
//}
