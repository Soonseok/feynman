package dev.soon.feynman.arpltnStatsSvc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soon.feynman.arpltnStatsSvc.api.DailyStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import dev.soon.feynman.arpltnStatsSvc.dto.DailyStatsApiResponse;
import dev.soon.feynman.config.SafeParseDouble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManualDailyStatsService {

    private final ObjectMapper objectMapper;
    private final ArpltnStatsDao arpltnStatsDao;
    private final DailyStatsApiClient dailyStatsApiClient;

    public void processDailyStats(String stationFileNum, String startStation, String endStation) throws IOException {
        String fileName = "/static/api_files/arpltn/stationName" + stationFileNum + ".json";

        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            if (is == null) {
                log.error("파일을 찾을 수 없습니다: {}", fileName);
                throw new IOException("파일이 존재하지 않습니다.");
            }
            List<String> stationNames = objectMapper.readValue(is, new TypeReference<>() {});

            int startIndex = (startStation != null) ? stationNames.indexOf(startStation) : 0;
            int endIndex = (endStation != null) ? stationNames.indexOf(endStation) : stationNames.size() - 1;

            if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
                throw new IllegalArgumentException("시작/종료 측정소 이름이 목록에 없거나 순서가 잘못되었습니다.");
            }

            List<String> subList = stationNames.subList(startIndex, endIndex + 1);

            int totalCount = subList.size();
            int successCount = 0;
            String lastSuccessfulStation = "None";

            log.info("총 요청 측정소 수: {}", totalCount);

            for (String station : subList) {
                try {
                    LocalDateTime lastDate = arpltnStatsDao.findLastMeasurementDate(station);
                    LocalDate start = (lastDate != null) ? lastDate.toLocalDate().plusDays(1) : LocalDate.now().minusMonths(1);
                    LocalDate end = LocalDate.now().minusDays(1);

                    if (start.isAfter(end)) {
                        log.info("측정소 '{}'는 이미 최신 데이터로 업데이트되었습니다.", station);
                        successCount++;
                        lastSuccessfulStation = station;
                        continue;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String startDate = start.format(formatter);
                    String endDate = end.format(formatter);

                    log.info("측정소 '{}' 데이터 가져오기: {}부터 {}까지", station, startDate, endDate);

                    int savedCount = getAndSaveData(station, startDate, endDate);

                    log.info("측정소 '{}'의 데이터 {}건을 성공적으로 저장했습니다.", station, savedCount);
                    successCount++;
                    lastSuccessfulStation = station;

                    TimeUnit.SECONDS.sleep(1);

                } catch (Exception e) {
                    int failedCount = totalCount - successCount;
                    log.error("\n=======================================================\n" +
                                    "측정소 코드 매처에서 문제가 발생했습니다.\n" +
                                    "요청 총계: {}개, 성공: {}개, 실패: {}개\n" +
                                    "마지막으로 성공한 측정소: {}\n" +
                                    "멈춘 원인: {}\n" +
                                    "에러 메시지: {}\n" +
                                    "=======================================================",
                            totalCount, successCount, failedCount, lastSuccessfulStation, e.getClass().getName(), e.getMessage(), e);
                    throw new RuntimeException("데이터 처리 중 오류 발생, 작업 중단", e);
                }
            }
            log.info("모든 수동 작업이 성공적으로 완료되었습니다. 총 {}개 측정소 처리.", totalCount);
        }
    }

    private int getAndSaveData(String msrstnName, String inqBginDt, String inqEndDt) throws Exception {
        int savedCount = 0;
        int numOfRows = 250;
        int pageNo = 1;

        DailyStatsApiResponse firstResponse = dailyStatsApiClient.callApi(msrstnName, inqBginDt, inqEndDt, numOfRows, pageNo);
        if (firstResponse == null || firstResponse.getResponse() == null || firstResponse.getResponse().getBody() == null) {
            log.warn("API 응답이 비어있거나 형식이 잘못되었습니다.");
            return 0;
        }

        int totalCount = firstResponse.getResponse().getBody().getTotalCount();
        if (totalCount == 0) {
            return 0;
        }

        savedCount += processAndSaveItems(firstResponse.getResponse().getBody().getItems());

        int totalPages = (int) Math.ceil((double) totalCount / numOfRows);
        for (int i = 2; i <= totalPages; i++) {
            DailyStatsApiResponse nextPageResponse = dailyStatsApiClient.callApi(msrstnName, inqBginDt, inqEndDt, numOfRows, i);
            if (nextPageResponse != null && nextPageResponse.getResponse().getBody() != null) {
                savedCount += processAndSaveItems(nextPageResponse.getResponse().getBody().getItems());
            }
        }
        return savedCount;
    }

    private int processAndSaveItems(List<DailyStatsApiResponse.Item> items) throws Exception {
        if (items == null) {
            return 0;
        }

        List<ArpltnStatsResponse> dailyStatsResponses = items.stream()
                .map(item -> ArpltnStatsResponse.builder()
                        .stationName(item.getMsrstnName())
                        .measurementDateTime(LocalDate.parse(item.getMsurDt()).atStartOfDay())
                        .dataType("daily")
                        .so2(SafeParseDouble.safeParseDouble(item.getSo2Value()))
                        .co(SafeParseDouble.safeParseDouble(item.getCoValue()))
                        .o3(SafeParseDouble.safeParseDouble(item.getO3Value()))
                        .no2(SafeParseDouble.safeParseDouble(item.getNo2Value()))
                        .pm10(SafeParseDouble.safeParseDouble(item.getPm10Value()))
                        .pm25(SafeParseDouble.safeParseDouble(item.getPm25Value()))
                        .build())
                .collect(Collectors.toList());

        for (ArpltnStatsResponse response : dailyStatsResponses) {
            arpltnStatsDao.insertArpltnStatsResponse(response);
        }
        return dailyStatsResponses.size();
    }
}