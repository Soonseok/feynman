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
import java.util.ArrayList;
import java.util.List;

/**
 * 실제로 외부 API에 HTTP 요청을 보내는 로직을 담고 있음
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyStatsApiClientImpl implements DailyStatsApiClient {

    @Value("${api.airkorea.daily.endpoint}")
    private String endpoint;

    @Value("${api.airkorea.service-key-encoding}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    @Override
    public DailyStatsApiResponse getDailyStats(String msrstnName, String inqBginDt, String inqEndDt) {
        int pageNo = 1;
        int numOfRows = 250;

        DailyStatsApiResponse firstResponse = callApi(msrstnName, inqBginDt, inqEndDt, numOfRows, pageNo);
        if (firstResponse == null || firstResponse.getResponse() == null) {
            return null;
        }

        int totalCount = firstResponse.getResponse().getBody().getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount/numOfRows);

        List<DailyStatsApiResponse.Item> allItems = new ArrayList<>();

        if (firstResponse.getResponse().getBody().getItems() != null) {
            allItems.addAll(firstResponse.getResponse().getBody().getItems());
        }

        for (int i = 2; i <= totalPages; i++) {
            DailyStatsApiResponse nextPageResponse = callApi(msrstnName, inqBginDt, inqEndDt, numOfRows, i);
            if (nextPageResponse != null && nextPageResponse.getResponse().getBody().getItems() != null) {
                allItems.addAll(nextPageResponse.getResponse().getBody().getItems());
            }
        }

        DailyStatsApiResponse finalResponse = new DailyStatsApiResponse();
        DailyStatsApiResponse.Body body = new DailyStatsApiResponse.Body();
        body.setItems(allItems);
        body.setTotalCount(totalCount);

        DailyStatsApiResponse.Response response = new DailyStatsApiResponse.Response();
        response.setBody(body);

        finalResponse.setResponse(response);
        return finalResponse;
    }

    private DailyStatsApiResponse callApi(String msrstnName, String inqBginDt, String inqEndDt, int numOfRows, int pageNo) {
        /**
         * 지금 api 요청 보낼 때 생기는 가장 큰 문제가
         * String stationName = "%EA%B0%95%EB%82%A8%EA%B5%AC";
         * 이렇게 보내면 정상적인 회신이 오는데,
         * String stationName = "강남구";
         * 이렇게 보내면 java.lang.IllegalArgumentException: Invalid character '강' for QUERY_PARAM in "강남구" 이 에러가 뜸.
         * 그래서 msrstnName만 수동으로 인코딩 해서 보내기로 함.
         */
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
            log.info("API 호출 시작: {}", uri);
            return restTemplate.getForObject(uri, DailyStatsApiResponse.class);
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("API 호출 실패", e);
        }
    }
}
