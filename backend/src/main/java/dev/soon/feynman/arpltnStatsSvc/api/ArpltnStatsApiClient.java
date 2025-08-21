package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 실제로 외부 API에 HTTP 요청을 보내는 로직을 담고 있음
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ArpltnStatsApiClient implements ArpltnStatsApi{

    @Value("${api.airkorea.endpoint}")
    private String endpoint;

    @Value("${api.airkorea.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    @Override
    public ArpltnStatsApiResponse getArpltnStats(String msrstnName, String inqBginDt, String inqEndDt) {
        URI uri = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("inqBginDt", inqBginDt)
                .queryParam("inqEndDt", inqEndDt)
                .queryParam("msrstnName", msrstnName)
                .build(false)
                .encode()
                .toUri();
        try {
            log.info("API 호출 시작: {}", uri);
            return restTemplate.getForObject(uri, ArpltnStatsApiResponse.class);
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }
}
