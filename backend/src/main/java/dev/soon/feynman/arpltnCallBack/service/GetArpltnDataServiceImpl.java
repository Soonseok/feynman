package dev.soon.feynman.arpltnCallBack.service;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityDataWithStationCode;
import dev.soon.feynman.arpltnCallBack.dto.TotalArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.dto.search.PaginatedArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.dto.search.SearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetArpltnDataServiceImpl implements GetArpltnDataService {

    private final GetArpltnData getArpltnData;

    @Override
    public PaginatedArpltnResponseDto searchAirPollutionData(SearchCriteriaDto searchCriteriaDto, int page) {
        int pageSize = 30;
        int startNum = page * pageSize;

        Map<String, Object> params = new HashMap<>();
        searchCriteriaDto.getStartDate().ifPresent(v -> params.put("startDate", v));
        searchCriteriaDto.getEndDate().ifPresent(v -> params.put("endDate", v));
        searchCriteriaDto.getStationName().ifPresent(v -> params.put("stationName", v));
        searchCriteriaDto.getStationCode().ifPresent(v -> params.put("stationCode", v));
        searchCriteriaDto.getDataType().ifPresent(v -> params.put("dataType", v));
        searchCriteriaDto.getMeasurementType().ifPresent(v -> params.put("measurementType", v));
        log.info("\n>>> params : {}", params.toString());

        Integer numberOfSearchedData = getArpltnData.getNumberOfSimpleSearchedData(params);
        List<AirQualityDataWithStationCode> searchedDataList = getArpltnData.getSimpleSearchedData(params, startNum, pageSize);

        List<TotalArpltnResponseDto> totalArpltnResponseDtos = searchedDataList.stream()
                .map(searchedData -> TotalArpltnResponseDto.builder()
                        .stationName(searchedData.getSido())
                        .stationCode(searchedData.getStationCode())
                        .airQualityData(searchedData)
                        .build())
                .collect(Collectors.toList());

        return PaginatedArpltnResponseDto.builder()
                .arpltnResponseList(totalArpltnResponseDtos)
                .totalCount(numberOfSearchedData)
                .build();
    }
}