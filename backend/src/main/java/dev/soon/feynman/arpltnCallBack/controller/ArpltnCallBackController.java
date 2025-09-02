package dev.soon.feynman.arpltnCallBack.controller;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityData;
import dev.soon.feynman.arpltnCallBack.dto.ApiResponse;
import dev.soon.feynman.arpltnCallBack.service.GetArpltnDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/arpltn-call")
public class ArpltnCallBackController {

    private final GetArpltnDataService getArpltnDataService;
    private final GetArpltnData getArpltnData;

    @GetMapping("/test")
    public ApiResponse<List<AirQualityData>> testCallBack() {
        List<AirQualityData> data = getArpltnDataService.getTestDataService();
        return new ApiResponse<>("Success", data.size(), data);
    }
}
