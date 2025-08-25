package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.api.MonthlyStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import dev.soon.feynman.arpltnStatsSvc.dto.MonthlyStatsApiResponse;
import dev.soon.feynman.config.SafeParseDouble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonthlyStatsServiceImpl implements MonthlyStatsService {

    private final MonthlyStatsApiClient monthlyStatsApiClient;
    private final ArpltnStatsDao arpltnStatsDao;

    @Override
    public List<ArpltnStatsResponse> getMonthlyStats(String msrstnName, String inqBginMm, String inqEndMm) {
        log.info("Starting monthly stats data retrieval for station: {}", msrstnName);

        MonthlyStatsApiResponse apiResponse = monthlyStatsApiClient.getMonthlyStats(msrstnName, inqBginMm, inqEndMm);

        if (apiResponse == null || apiResponse.getResponse() == null || apiResponse.getResponse().getBody() == null) {
            log.warn("API response is empty or malformed.");
            return Collections.emptyList();
        }

        List<MonthlyStatsApiResponse.Item> items = apiResponse.getResponse().getBody().getItems();

        if (items == null) {
            return Collections.emptyList();
        }

        List<ArpltnStatsResponse> monthlyStatsResponses = items.stream()
                .map(item -> {
                    // msurMm 필드를 LocalDateTime으로 변환. (예: "2020-07" -> 2020-07-01 00:00:00)
                    LocalDateTime measurementDateTime = null;
                    try {
                        YearMonth yearMonth = YearMonth.parse(item.getMsurMm(), DateTimeFormatter.ofPattern("yyyy-MM"));
                        measurementDateTime = yearMonth.atDay(1).atStartOfDay();
                    } catch (Exception e) {
                        log.error("Failed to parse date from item: {}", item.getMsurMm(), e);
                    }

                    return ArpltnStatsResponse.builder()
                            .dataType("monthly")
                            .stationName(item.getMsrstnName())
                            .measurementDateTime(measurementDateTime)
                            .so2(SafeParseDouble.safeParseDouble(item.getSo2Value()))
                            .co(SafeParseDouble.safeParseDouble(item.getCoValue()))
                            .o3(SafeParseDouble.safeParseDouble(item.getO3Value()))
                            .no2(SafeParseDouble.safeParseDouble(item.getNo2Value()))
                            .pm10(SafeParseDouble.safeParseDouble(item.getPm10Value()))
                            .pm25(SafeParseDouble.safeParseDouble(item.getPm25Value()))
                            .build();
                })
                .collect(Collectors.toList());

        for (ArpltnStatsResponse response : monthlyStatsResponses) {
            arpltnStatsDao.insertArpltnStatsResponse(response);
        }

        log.info("Successfully processed and saved {} monthly stats records.", monthlyStatsResponses.size());
        return monthlyStatsResponses;
    }
}
