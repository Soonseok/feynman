package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.MonthlyStatsApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonthlyStatsApiClientImpl implements MonthlyStatsApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.airkorea.monthly.endpoint}")
    private String baseUrl;

    @Value("${api.airkorea.service-key-encoding}")
    private String serviceKey;

    @Override
    public MonthlyStatsApiResponse getMonthlyStats(String msrstnName, String inqBginMm, String inqEndMm) {

        String encodedMsrstnName = UriUtils.encode(msrstnName, StandardCharsets.UTF_8);
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("msrstnName", encodedMsrstnName)
                .queryParam("inqBginMm", inqBginMm)
                .queryParam("inqEndMm", inqEndMm)
                .build(true)
                .toUri();

        log.info("Calling MonthlyStats API with URI: {}", uri);

        try {
            return restTemplate.getForObject(uri, MonthlyStatsApiResponse.class);
        } catch (Exception e) {
            log.error("Failed to call MonthlyStats API: {}", e.getMessage());
            throw new RuntimeException("API 호출 실패", e);
        }
    }
}
