package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.api.SggStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import dev.soon.feynman.arpltnStatsSvc.dto.SggStatsApiResponse;
import dev.soon.feynman.config.SafeParseDouble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SggStatsServiceImpl implements SggStatsService {

    private final SggStatsApiClient sggStatsApiClient;
    private final ArpltnStatsDao arpltnStatsDao;

    @Override
    public List<ArpltnStatsResponse> getSggStats(String sidoName, String searchCondition) {
        log.info("Starting hourly stats data retrieval for sido: {}", sidoName);

        SggStatsApiResponse apiResponse = sggStatsApiClient.getSggStats(sidoName, searchCondition);

        if (apiResponse == null || apiResponse.getResponse() == null || apiResponse.getResponse().getBody() == null) {
            log.warn("API response is empty or malformed.");
            throw new RuntimeException("API 응답이 유효하지 않습니다.");
        }

        List<SggStatsApiResponse.Item> items = apiResponse.getResponse().getBody().getItems();

        if (items == null) {
            return Collections.emptyList();
        }

        List<ArpltnStatsResponse> hourlyStatsResponses = items.stream()
                .map(item -> {
                    // dataTime 필드를 LocalDateTime으로 변환. (예: "2025-08-22 16:00")
                    LocalDateTime measurementDateTime = null;
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        measurementDateTime = LocalDateTime.parse(item.getDataTime(), formatter);
                    } catch (Exception e) {
                        log.error("Failed to parse date from item: {}", item.getDataTime(), e);
                    }

                    return ArpltnStatsResponse.builder()
                            .dataType("hourly")
                            .stationName(item.getCityName())
                            .measurementDateTime(measurementDateTime)
                            .so2(SafeParseDouble.safeParseDouble(item.getSo2Value()))
                            .co(SafeParseDouble.safeParseDouble(item.getCoValue()))
                            .o3(SafeParseDouble.safeParseDouble(item.getO3Value()))
                            .no2(SafeParseDouble.safeParseDouble(item.getNo2Value()))
                            .pm10(SafeParseDouble.safeParseDouble(item.getPm10Value()))
                            .pm25(SafeParseDouble.safeParseDouble(item.getPm25Value()))
                            .khai(SafeParseDouble.safeParseDouble(item.getKhaiValue()))
                            .build();
                })
                .collect(Collectors.toList());

        for (ArpltnStatsResponse response : hourlyStatsResponses) {
            arpltnStatsDao.insertArpltnStatsResponse(response);
        }

        log.info("Successfully processed and saved {} hourly stats records.", hourlyStatsResponses.size());
        return hourlyStatsResponses;
    }
}
