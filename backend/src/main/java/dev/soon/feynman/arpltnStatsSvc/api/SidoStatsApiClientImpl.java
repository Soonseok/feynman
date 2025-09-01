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

    @Value("${api.service-key-encoding}")
    private String serviceKey;

    @Override
    public SidoStatsApiResponse callApi(String itemCode, String dataGubun, String searchCondition, int numOfRows, int pageNo) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("itemCode", itemCode)
                .queryParam("dataGubun", dataGubun)
                .queryParam("searchCondition", searchCondition)
                .build(true)
                .toUri();

        try {
            log.info("SidoStats API 호출 시작: {}", uri);
            return restTemplate.getForObject(uri, SidoStatsApiResponse.class);
        } catch (Exception e) {
            log.error("SidoStats API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("SidoStats API 호출 실패", e);
        }
    }
}