package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.api.DailyStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import dev.soon.feynman.arpltnStatsSvc.dto.DailyStatsApiResponse;
import dev.soon.feynman.config.SafeParseDouble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DailyStatsApiClient로부터 받은 응답을 처리하는 로직
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DailyStatsServiceImpl implements DailyStatsService {

    private final DailyStatsApiClient dailyStatsApiClient;
    private final ArpltnStatsDao arpltnStatsDao;

    @Override
    @Async
    public void getDailyStats(String msrstnName, String inqBginDt, String inqEndDt) {
        log.info("Starting daily stats data retrieval for station: {}", msrstnName);
        int numOfRows = 250;
        int pageNo = 1;
        DailyStatsApiResponse firstResponse = dailyStatsApiClient.callApi(msrstnName, inqBginDt, inqEndDt, numOfRows, pageNo);

        if (firstResponse == null || firstResponse.getResponse() == null) {
            log.warn("DailyStatsApiResponse is empty or malformed.");
            return;
        }

        int totalCount = firstResponse.getResponse().getBody().getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount/numOfRows);

        processAndSaveItems(firstResponse.getResponse().getBody().getItems());

        for (int i = 2; i <= totalPages; i++) {
            DailyStatsApiResponse nextPageResponse = dailyStatsApiClient.callApi(msrstnName, inqBginDt, inqEndDt, numOfRows, i);
            if (nextPageResponse != null && nextPageResponse.getResponse().getBody().getItems() != null) {
                processAndSaveItems(nextPageResponse.getResponse().getBody().getItems());
            }
        }
        log.info("Successfully processed and saved {} daily stats records.", totalCount);
    }

    /**
     * API 응답의 원본 DTO를 우리가 정의한 표준화 DTO로 변환한다
     * @param items
     */
    public void processAndSaveItems(List<DailyStatsApiResponse.Item> items) {
        if (items == null) {
            return;
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
    }
}
