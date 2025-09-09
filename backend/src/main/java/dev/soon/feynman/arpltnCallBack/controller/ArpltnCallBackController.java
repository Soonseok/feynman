package dev.soon.feynman.arpltnCallBack.controller;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dao.GetDistrict;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityData;
import dev.soon.feynman.arpltnCallBack.dto.ApiResponse;
import dev.soon.feynman.arpltnCallBack.dto.DistrictData;
import dev.soon.feynman.arpltnCallBack.dto.TotalArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.service.GetArpltnDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/arpltn-call")
public class ArpltnCallBackController {

    private final GetArpltnDataService getArpltnDataService;
    private final GetArpltnData getArpltnData;
    private final GetDistrict getDistrict;

    @GetMapping("/test")
    public ApiResponse<List<AirQualityData>> testCallBack() {
        List<AirQualityData> data = getArpltnDataService.getTestDataService();
        return new ApiResponse<>("Success", data.size(), data);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<TotalArpltnResponseDto>> getDistrictByCode(@PathVariable String code) {
        DistrictData districtData = getDistrict.getDistrictDataByCode(code);
        if (districtData == null) {
            return ResponseEntity.notFound().build();
        }
        AirQualityData airQualityData = getArpltnData.getTestDataByCode(code);
        TotalArpltnResponseDto totalArpltnResponseDto = TotalArpltnResponseDto.builder()
                .stationName(districtData.getDistrictName())
                .stationCode(districtData.getDistrictCode())
                .airQualityData(airQualityData)
                .build();

        ApiResponse<TotalArpltnResponseDto> apiResponse = new ApiResponse<>("Success", 1, totalArpltnResponseDto);
        return ResponseEntity.ok(apiResponse);
    }
}
