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

    @Value("${api.service-key-encoding}")
    private String serviceKey;

    @Override
    public MonthlyStatsApiResponse callApi(String msrstnName, String inqBginMm, String inqEndMm, int numOfRows, int pageNo) {

        String encodedMsrstnName = UriUtils.encode(msrstnName, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("msrstnName", encodedMsrstnName)
                .queryParam("inqBginMm", inqBginMm)
                .queryParam("inqEndMm", inqEndMm)
                .build(true)
                .toUri();

        try {
            log.info("MonthlyStats API 호출 시작: {}", uri);
            return restTemplate.getForObject(uri, MonthlyStatsApiResponse.class);
        } catch (Exception e) {
            log.error("MonthlyStats API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("MonthlyStats API 호출 실패", e);
        }
    }
}
