package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.api.ArpltnStatsApiClient;
import dev.soon.feynman.arpltnStatsSvc.dao.ArpltnStatsDao;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsApiResponse;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsApiResponse.Item;
import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ArpltnStatsApiClient로부터 받은 응답을 처리하는 로직
 */
@Service
@RequiredArgsConstructor
public class ArpltnStatsServiceImpl implements ArpltnStatsService{

    private final ArpltnStatsApiClient arpltnStatsApiClient;
    private final ArpltnStatsDao arpltnStatsDao;

    @Override
    public List<ArpltnStatsResponse> getArpltnStats(String msrstnName, String inqBginDt, String inqEndDt) {
        // API 클라이언트를 호출하여 API 응답(원본 DTO)을 받는다
        ArpltnStatsApiResponse apiResponse = arpltnStatsApiClient.getArpltnStats(msrstnName, inqBginDt, inqEndDt);

        if (apiResponse == null || apiResponse.getResponse() == null || apiResponse.getResponse().getBody() == null) {
            return Collections.emptyList();
        }

        List<Item> items = apiResponse.getResponse().getBody().getItems();

        if (items == null) {
            return Collections.emptyList();
        }

        // API 응답의 원본 DTO를 우리가 정의한 표준화 DTO로 변환한다
        List<ArpltnStatsResponse> arpltnStatsResponses = items.stream()
                .map(item -> ArpltnStatsResponse.builder()
                        .stationName(item.getMsrstnName())
                        .measurementDate(LocalDate.parse(item.getMsurDt()))
                        .so2(Double.parseDouble(item.getSo2Value()))
                        .co(Double.parseDouble(item.getCoValue()))
                        .o3(Double.parseDouble(item.getO3Value()))
                        .no2(Double.parseDouble(item.getNo2Value()))
                        .pm10(Double.parseDouble(item.getPm10Value()))
                        .pm25(Double.parseDouble(item.getPm25Value()))
                        .build())
                .collect(Collectors.toList());

        for (ArpltnStatsResponse response : arpltnStatsResponses) {
            arpltnStatsDao.insertArpltnStatsResponse(response);
        }

        return arpltnStatsResponses;
    }
}
