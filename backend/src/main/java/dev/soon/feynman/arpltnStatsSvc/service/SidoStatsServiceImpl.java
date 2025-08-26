package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.api.SidoStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import dev.soon.feynman.arpltnStatsSvc.dto.SidoStatsApiResponse;
import dev.soon.feynman.config.SafeParseDouble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 고정된 searchCondition 로직 주의. <br/>
 * dataGubun=DAILY&searchCondition=WEEK로 보내면  <br/>
 * "totalCount": 7, "dataTime": "2025-08-25"~"dataTime": "2025-08-19" 값이 들어오고, <br/>
 * dataGubun=DAILY&searchCondition=MONTH로 보내면  <br/>
 * "totalCount": 31, "dataTime": "2025-08-25"~"dataTime": "2025-07-26"이 들어와서 <br/>
 * 결국 MONTH가 WEEK을 포함해서 상관이 없음. <br/>
 * 그리고 dataGubun=HOUR는 어차피 searchCondition과 상관없이 똑같은 값인 최근 25개를 가지고 와서 상관이 없음.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SidoStatsServiceImpl implements SidoStatsService {

    private final SidoStatsApiClient sidoStatsApiClient;
    private final ArpltnStatsDao arpltnStatsDao;

    @Override
    public void getSidoStats(String itemCode, String dataGubun, String searchCondition) {
        log.info("Starting sido stats data retrieval for item: {}", itemCode);
        int numOfRows = 250;
        int pageNo = 1;
        SidoStatsApiResponse firstResponse = sidoStatsApiClient.callApi(itemCode, dataGubun, searchCondition, numOfRows, pageNo);

        if (firstResponse == null || firstResponse.getResponse() == null) {
            log.warn("SidoStatsApiResponse is empty or malformed.");
            return;
        }

        int totalCount = firstResponse.getResponse().getBody().getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount/numOfRows);

        processAndSaveItems(firstResponse.getResponse().getBody().getItems(), dataGubun);

        for (int i = 2; i <= totalPages; i++) {
            SidoStatsApiResponse nextPageResponse = sidoStatsApiClient.callApi(itemCode, dataGubun, searchCondition, numOfRows, i);
            if (nextPageResponse != null && nextPageResponse.getResponse().getBody().getItems() != null) {
                processAndSaveItems(nextPageResponse.getResponse().getBody().getItems(), dataGubun);
            }
        }
        log.info("Successfully processed and saved {} sido stats records.", totalCount);
    }

    public void processAndSaveItems(List<SidoStatsApiResponse.Item> items, String dataGubun) {
        if (items == null) {
            return;
        }
        Map<String, ArpltnStatsResponse.ArpltnStatsResponseBuilder> builderMap = new HashMap<>();
        for (SidoStatsApiResponse.Item item : items) {
            LocalDateTime measurementDateTime = null;
            String dataGubunValue = dataGubun.toLowerCase();

            try {
                if ("daily".equals(dataGubunValue)) {
                    measurementDateTime = LocalDate.parse(item.getDataTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                } else if ("hour".equals(dataGubunValue)) {
                    measurementDateTime = LocalDateTime.parse(item.getDataTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }
            } catch (Exception e) {
                log.error("Failed to parse date from item: {}", item.getDataTime(), e);
                continue;
            }
            if (measurementDateTime == null) {
                log.warn("measurementDateTime is null after parsing. Skipping item: {}", item);
                continue;
            }

            Map<String, String> cityData = getCityData(item);
            String itemCodeValue = item.getItemCode();
            String dataType = item.getDataGubun().equalsIgnoreCase("HOUR") ? "hourly" : "daily";

            for (Map.Entry<String, String> entry : cityData.entrySet()) {
                String cityName = entry.getKey();
                String value = entry.getValue();
                String mapKey = measurementDateTime.toString() + "_" + cityName + "_" + dataType;

                builderMap.putIfAbsent(mapKey, ArpltnStatsResponse.builder()
                        .dataType(dataType)
                        .stationName(cityName)
                        .measurementDateTime(measurementDateTime));

                ArpltnStatsResponse.ArpltnStatsResponseBuilder builder = builderMap.get(mapKey);
                setPollutionValue(builder, itemCodeValue, value);
            }
        }

        List<ArpltnStatsResponse> sidoStatsResponses = builderMap.values().stream()
                .map(ArpltnStatsResponse.ArpltnStatsResponseBuilder::build)
                .collect(Collectors.toList());

        for (ArpltnStatsResponse response : sidoStatsResponses) {
            arpltnStatsDao.insertArpltnStatsResponse(response);
        }
    }

    private Map<String, String> getCityData(SidoStatsApiResponse.Item item) {
        Map<String, String> cityData = new LinkedHashMap<>();
        cityData.put("서울", item.getSeoul());
        cityData.put("부산", item.getBusan());
        cityData.put("대구", item.getDaegu());
        cityData.put("인천", item.getIncheon());
        cityData.put("광주", item.getGwangju());
        cityData.put("대전", item.getDaejeon());
        cityData.put("울산", item.getUlsan());
        cityData.put("경기", item.getGyeonggi());
        cityData.put("강원", item.getGangwon());
        cityData.put("충북", item.getChungbuk());
        cityData.put("충남", item.getChungnam());
        cityData.put("전북", item.getJeonbuk());
        cityData.put("전남", item.getJeonnam());
        cityData.put("경북", item.getGyeongbuk());
        cityData.put("경남", item.getGyeongnam());
        cityData.put("제주", item.getJeju());
        cityData.put("세종", item.getSejong());
        return cityData;
    }

    private void setPollutionValue(ArpltnStatsResponse.ArpltnStatsResponseBuilder builder, String itemCode, String value) {
        Double doubleValue = SafeParseDouble.safeParseDouble(value);
        if (doubleValue == null) {
            return;
        }

        switch (itemCode.toUpperCase()) {
            case "SO2": builder.so2(doubleValue); break;
            case "CO": builder.co(doubleValue); break;
            case "O3": builder.o3(doubleValue); break;
            case "NO2": builder.no2(doubleValue); break;
            case "PM10": builder.pm10(doubleValue); break;
            case "PM2.5": builder.pm25(doubleValue); break;
        }
    }
}