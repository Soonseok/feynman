package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.DailyStatsApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 실제로 외부 API에 HTTP 요청을 보내는 로직을 담고 있음
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyStatsApiClientImpl implements DailyStatsApiClient {

    @Value("${api.airkorea.daily.endpoint}")
    private String endpoint;

    @Value("${api.service-key-encoding}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    @Override
    public DailyStatsApiResponse callApi(String msrstnName, String inqBginDt, String inqEndDt, int numOfRows, int pageNo) {
        String encodedMsrstnName = UriUtils.encode(msrstnName, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("inqBginDt", inqBginDt)
                .queryParam("inqEndDt", inqEndDt)
                .queryParam("msrstnName", encodedMsrstnName)
                .build(true)
                .toUri();

        try {
            log.info("DailyStats API 호출 시작: {}", uri);
            return restTemplate.getForObject(uri, DailyStatsApiResponse.class);
        } catch (Exception e) {
            log.error("DailyStats API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("DailyStats API 호출 실패", e);
        }
    }
}
