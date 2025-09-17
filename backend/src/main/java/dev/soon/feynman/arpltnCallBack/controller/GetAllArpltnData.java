package dev.soon.feynman.arpltnCallBack.controller;

import dev.soon.feynman.arpltnCallBack.dto.ApiResponse;
import dev.soon.feynman.arpltnCallBack.dto.search.PaginatedArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.dto.search.SearchCriteriaDto;
import dev.soon.feynman.arpltnCallBack.service.GetArpltnDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/arpltn-search")
public class GetAllArpltnData {

    private final GetArpltnDataService getArpltnDataService;

    @PostMapping("/searchData")
    public ResponseEntity<ApiResponse<PaginatedArpltnResponseDto>> searchData(
            @RequestBody SearchCriteriaDto searchCriteriaDto,
            @RequestParam(defaultValue = "0") int page){

        PaginatedArpltnResponseDto paginatedData = getArpltnDataService.searchAirPollutionData(searchCriteriaDto, page);

        ApiResponse<PaginatedArpltnResponseDto> returnData = new ApiResponse<>(
                "Success",
                paginatedData.getArpltnResponseList().size(),
                paginatedData.getTotalCount(),
                paginatedData
        );

        return ResponseEntity.ok(returnData);
    }
}