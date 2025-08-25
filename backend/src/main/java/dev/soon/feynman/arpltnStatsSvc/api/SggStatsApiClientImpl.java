package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.SggStatsApiResponse;
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
public class SggStatsApiClientImpl implements SggStatsApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.airkorea.sgg.endpoint}")
    private String baseUrl;

    @Value("${api.airkorea.service-key-encoding}")
    private String serviceKey;

    @Override
    public SggStatsApiResponse getSggStats(String sidoName, String searchCondition) {
        String encodedSidoName = UriUtils.encode(sidoName, StandardCharsets.UTF_8);
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("sidoName", encodedSidoName)
                .queryParam("searchCondition", searchCondition)
                .build(true)
                .toUri();

        log.info("Calling SggStats API with URI: {}", uri);

        try {
            return restTemplate.getForObject(uri, SggStatsApiResponse.class);
        } catch (Exception e) {
            log.error("Failed to call SggStats API: {}", e.getMessage());
            throw new RuntimeException("API 호출 실패", e);
        }
    }
}