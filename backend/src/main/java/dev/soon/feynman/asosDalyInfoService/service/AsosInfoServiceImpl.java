package dev.soon.feynman.asosDalyInfoService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soon.feynman.asosDalyInfoService.api.AsosInfoApiClient;
import dev.soon.feynman.asosDalyInfoService.dao.AsosInfoDao;
import dev.soon.feynman.asosDalyInfoService.dto.AsosDalyInfoApiResponse;
import dev.soon.feynman.asosDalyInfoService.dto.AsosInfoResponse;
import dev.soon.feynman.config.SafeParseDouble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsosInfoServiceImpl implements AsosInfoService{

    private final AsosInfoApiClient asosInfoApiClient;
    private final AsosInfoDao asosInfoDao;

    @Override
    @Async
    public void getDailyStats(String stnIds, String startDt, String endDt) {
        try {
            String stationName = getStationNameFromStnIds(stnIds);
            log.info("Starting daily ASOS date retrieval for station: {}", stationName);
        } catch (IOException e) {
            log.error("Failed to find station name for station id: {}", stnIds, e);
        }
        int numOfRows = 250;
        int pageNo = 1;
        AsosDalyInfoApiResponse firstResponse = asosInfoApiClient.callApi(stnIds, startDt, endDt, numOfRows, pageNo);

        if (firstResponse == null || firstResponse.getResponse() == null) {
            log.warn("AsosDalyInfoApiResponse is empty or malformed.");
            return;
        }

        int totalCount = firstResponse.getResponse().getBody().getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount/numOfRows);

        processAndSaveItems(firstResponse.getResponse().getBody().getItems().getItem());

        for (int i = 2; i <= totalPages; i++) {
            AsosDalyInfoApiResponse nextPageResponse = asosInfoApiClient.callApi(stnIds, startDt, endDt, numOfRows, i);
            if (nextPageResponse != null && nextPageResponse.getResponse().getBody().getItems().getItem() != null) {
                processAndSaveItems(nextPageResponse.getResponse().getBody().getItems().getItem());
            }
        }
        log.info("Successfully processed and saved {} daily ASOS stats records", totalCount);
    }

    /**
     * 지점 번호(stnIds)를 받아 그에 맞는 지점명(stationName)을 반환한다.
     * @param stnIds 종관기상관측 지점 번호
     * @return 지점명(stationName)
     * @throws IOException
     */
    public String getStationNameFromStnIds(String stnIds) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/asos/asos_stations.json")) {
            if (is == null) {
                throw new IOException("File not found in classpath: static/asos/asos_stations.json");
            }
            JsonNode root = mapper.readTree(is);
            int targetId = Integer.parseInt(stnIds);
            String stationName = null;

            for (JsonNode node : root) {
                if (node.get("stationId").asInt() == targetId) {
                    stationName = node.get("stationName").asText();
                    break;
                }
            }
            return stationName;
        }
    }

    public void processAndSaveItems(List<AsosDalyInfoApiResponse.Item> items) {
        if (items == null || items.isEmpty()) {
            log.info("ASOS API에서 가져온 항목이 없습니다.");
            return;
        }
        List<AsosInfoResponse> asosResponses = items.stream()
                .map(item -> AsosInfoResponse.builder()
                        .measurementDateTime(LocalDate.parse(item.getTm(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay())
                        .stationId(item.getStnId())
                        .stationName(item.getStnNm())
                        .averageTemperature(SafeParseDouble.safeParseDouble(item.getAvgTa()))
                        .minimumTemperature(SafeParseDouble.safeParseDouble(item.getMinTa()))
                        .maximumTemperature(SafeParseDouble.safeParseDouble(item.getMaxTa()))
                        .dailyRainfall(SafeParseDouble.safeParseDouble(item.getSumRn()))
                        .averageWindSpeed(SafeParseDouble.safeParseDouble(item.getAvgWs()))
                        .maximumWindSpeed(SafeParseDouble.safeParseDouble(item.getMaxWs()))
                        .maximumInstantaneousWindSpeed(SafeParseDouble.safeParseDouble(item.getMaxInsWs()))
                        .averageHumidity(SafeParseDouble.safeParseDouble(item.getAvgRhm()))
                        .minimumHumidity(SafeParseDouble.safeParseDouble(item.getMinRhm()))
                        .averageSeaLevelPressure(SafeParseDouble.safeParseDouble(item.getAvgPs()))
                        .dailySunshineHours(SafeParseDouble.safeParseDouble(item.getSumSsHr()))
                        .totalGlobalSolarRadiation(SafeParseDouble.safeParseDouble(item.getSumGsr()))
                        .averageCloudAmount(SafeParseDouble.safeParseDouble(item.getAvgTca()))
                        .averageSoilTemperature5cm(SafeParseDouble.safeParseDouble(item.getAvgCm5Te()))
                        .averageSoilTemperature10cm(SafeParseDouble.safeParseDouble(item.getAvgCm10Te()))
                        .averageSoilTemperature20cm(SafeParseDouble.safeParseDouble(item.getAvgCm20Te()))
                        .averageSoilTemperature30cm(SafeParseDouble.safeParseDouble(item.getAvgCm30Te()))
                        .build())
                .collect(Collectors.toList());

        for (AsosInfoResponse response : asosResponses) {
            try {
                asosInfoDao.insertAsosInfoResponse(response);
            } catch (Exception e) {
                log.error("Failed to insert ASOS stats for station_id: {} on date: {}",
                        response.getStationId(), response.getMeasurementDateTime(), e);
            }
        }
    }
}
