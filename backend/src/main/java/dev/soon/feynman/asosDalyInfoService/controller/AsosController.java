package dev.soon.feynman.asosDalyInfoService.controller;

import dev.soon.feynman.asosDalyInfoService.service.AsosInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/asos")
public class AsosController {

    private final AsosInfoService asosInfoService;

    @GetMapping("/daily-info")
    public ResponseEntity<String> getDailyASOSInfo(
            @RequestParam String stnIds,
            @RequestParam String startDt,
            @RequestParam String endDt) {
        try {
            asosInfoService.getDailyStats(stnIds, startDt, endDt);
            return ResponseEntity.ok("ASOS 일일 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("ASOS 데이터 수집 중 오류 발생: "+ e.getMessage());
        }
    }
}
