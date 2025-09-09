package dev.soon.feynman.arpltnCallBack.controller;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dao.GetDistrict;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityData;
import dev.soon.feynman.arpltnCallBack.dto.ApiResponse;
import dev.soon.feynman.arpltnCallBack.dto.DistrictData;
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
    public ResponseEntity<Map<String, Object>> getDistrictByCode(@PathVariable String code) {
        DistrictData districtData = getDistrict.getDistrictDataByCode(code);
        AirQualityData airQualityData = getArpltnData.getTestDataByCode(code);
        
        if (districtData != null) {
            Map<String, Object> res = new HashMap<>();
            res.put("station_name", districtData.getDistrictName());
            res.put("station_code", districtData.getDistrictCode());
            if (airQualityData != null) {
                res.put("pm10_value", String.valueOf(airQualityData.getPm10_value()));
                res.put("dataType", airQualityData.getDataType());
                res.put("date", airQualityData.getDate());
            } else {
                res.put("pm10_value", "자료 없음");
                res.put("dataType", "자료 없음");
                res.put("date", "자료 없음");
            }
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
