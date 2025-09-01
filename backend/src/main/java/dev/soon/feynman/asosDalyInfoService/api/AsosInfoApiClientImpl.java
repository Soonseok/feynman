package dev.soon.feynman.asosDalyInfoService.api;

import dev.soon.feynman.asosDalyInfoService.dto.AsosDalyInfoApiResponse;
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
public class AsosInfoApiClientImpl implements AsosInfoApiClient{

    @Value("${api.asos.endpoint}")
    private String endpoint;

    @Value("${api.service-key-encoding}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    @Override
    public AsosDalyInfoApiResponse callApi(String stnIds, String startDt, String endDt, int numOfRows, int pageNo) {

        URI uri = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("dataType", "json")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("dataCd", "ASOS")
                .queryParam("dateCd", "DAY")
                .queryParam("stnIds", stnIds)
                .queryParam("startDt", startDt)
                .queryParam("endDt", endDt)
                .build(true)
                .toUri();

        try {
            log.info("AsosDalyInfo API 호출 시작: {}", uri);
            return restTemplate.getForObject(uri, AsosDalyInfoApiResponse.class);
        } catch (Exception e) {
            log.error("AsosDalyInfo API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("AsosDalyInfo API 호출 실패", e);
        }
    }
}
