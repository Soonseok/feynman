package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.SidoStatsApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class SidoStatsApiClientImpl implements SidoStatsApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.airkorea.sido.endpoint}")
    private String baseUrl;

    @Value("${api.airkorea.service-key-encoding}")
    private String serviceKey;

    @Override
    public SidoStatsApiResponse getSidoStats(String itemCode, String dataGubun, String searchCondition) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("itemCode", itemCode)
                .queryParam("dataGubun", dataGubun)
                .queryParam("searchCondition", searchCondition)
                .build(true)
                .toUri();

        log.info("Calling SidoStats API with URI: {}", uri);

        try {
            return restTemplate.getForObject(uri, SidoStatsApiResponse.class);
        } catch (Exception e) {
            log.error("Failed to call SidoStats API: {}", e.getMessage());
            throw new RuntimeException("API 호출 실패", e);
        }
    }
}