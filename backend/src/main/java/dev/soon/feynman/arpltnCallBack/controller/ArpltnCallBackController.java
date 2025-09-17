package dev.soon.feynman.arpltnCallBack.controller;

import dev.soon.feynman.arpltnCallBack.dto.ApiResponse;
import dev.soon.feynman.arpltnCallBack.dto.TotalArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.service.ArpltnCallBackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/arpltn-call")
public class ArpltnCallBackController {

    private final ArpltnCallBackService arpltnCallBackService;

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<TotalArpltnResponseDto>> getDistrictByCode(@PathVariable String code) {
        TotalArpltnResponseDto totalArpltnResponseDto = arpltnCallBackService.getAirPollutionDataByCode(code);
        if (totalArpltnResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        ApiResponse<TotalArpltnResponseDto> apiResponse = new ApiResponse<>("Success", 1, 1, totalArpltnResponseDto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/have_station")
    public ResponseEntity<List<String>> haveStationDistrict() {
        List<String> stationIncludedDistrictCodeList = arpltnCallBackService.getStationIncludedDistrictCodeList();
        return ResponseEntity.ok(stationIncludedDistrictCodeList);
    }
}