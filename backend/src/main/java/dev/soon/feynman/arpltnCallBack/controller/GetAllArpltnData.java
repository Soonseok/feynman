package dev.soon.feynman.arpltnCallBack.controller;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dao.GetDistrict;
import dev.soon.feynman.arpltnCallBack.dto.*;
import dev.soon.feynman.arpltnCallBack.dto.search.PaginatedArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.dto.search.SearchCriteriaDto;
import dev.soon.feynman.arpltnCallBack.service.GetArpltnDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/arpltn-search")
public class GetAllArpltnData {

    private final GetArpltnDataService getArpltnDataService;
    private final GetArpltnData getArpltnData;
    private final GetDistrict getDistrict;

    /**
     * 간단한 조건의 종합적인 검색을 위한 엔드포인트.
     * postman으로 테스트 할 때, body -> raw -> JSON으로 설정 해서 진짜 JSON 형식 맞춰 줘야 함
     * "measurementType": ["so2", "o3"] 얘는 List로 받기로 해서 이렇게 보내야 함
     * @param searchCriteriaDto
     * @return
     */
    @PostMapping("/searchData")
    public ResponseEntity<ApiResponse<PaginatedArpltnResponseDto>> searchData(
            @RequestBody SearchCriteriaDto searchCriteriaDto,
            @RequestParam(defaultValue = "0") int page){
        int pageSize = 30;
        int startNum = page * pageSize;
        // 들어온 요청의 파라미터를 DTO에 매칭
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
        PaginatedArpltnResponseDto paginatedArpltnResponseDto = PaginatedArpltnResponseDto.builder()
                .arpltnResponseList(totalArpltnResponseDtos)
                .build();
        ApiResponse<PaginatedArpltnResponseDto> returnData =  new ApiResponse<>("Success", searchedDataList.size(), numberOfSearchedData, paginatedArpltnResponseDto);
        return ResponseEntity.ok(returnData);
    }
}
