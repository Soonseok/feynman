package dev.soon.feynman.arpltnStatsSvc.controller;

import dev.soon.feynman.arpltnStatsSvc.service.ManualDailyStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/manually")
public class ManuallyRequestController {

    private final ManualDailyStatsService manualDailyStatsService;

    @GetMapping("/daily-stats")
    public ResponseEntity<String> runManualDailyStats(
            @RequestParam("file") String file,
            @RequestParam(value = "startStation", required = false) String startStation,
            @RequestParam(value = "endStation", required = false) String endStation) {

        log.info("Manual daily stats job requested for file: {}, from station: {}, to station: {}",
                file, startStation, endStation);

        try {
            manualDailyStatsService.processDailyStats(file, startStation, endStation);
            return ResponseEntity.ok("작업이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            log.error("수동 작업 중 심각한 오류가 발생하여 중단되었습니다.", e);
            return ResponseEntity.status(500).body("작업 실패: " + e.getMessage());
        }
    }
}