package dev.soon.feynman.arpltnStatsSvc.controller;

import dev.soon.feynman.arpltnStatsSvc.service.DailyStatsService;
import dev.soon.feynman.arpltnStatsSvc.service.MonthlyStatsService;
import dev.soon.feynman.arpltnStatsSvc.service.SggStatsService;
import dev.soon.feynman.arpltnStatsSvc.service.SidoStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/arpltn-stats")
public class ArpltnStatsController {

    private final DailyStatsService dailyStatsService;
    private final MonthlyStatsService monthlyStatsService;
    private final SggStatsService sggStatsService;
    private final SidoStatsService sidoStatsService;

    @GetMapping("/daily-stats")
    public ResponseEntity<String> getDailyStats(
            @RequestParam String msrstnName,
            @RequestParam String inqBginDt,
            @RequestParam String inqEndDt) {
        try {
            dailyStatsService.getDailyStats(msrstnName, inqBginDt, inqEndDt);
            return ResponseEntity.ok("일평균 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/monthly-stats")
    public ResponseEntity<String> getMonthlyStats(
            @RequestParam String msrstnName,
            @RequestParam String inqBginMm,
            @RequestParam String inqEndMm) {
        try {
            monthlyStatsService.getMonthlyStats(msrstnName, inqBginMm, inqEndMm);
            return ResponseEntity.ok("월평균 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/sgg-stats")
    public ResponseEntity<String> getSggStats(
            @RequestParam String sidoName,
            @RequestParam String searchCondition) {
        try {
            sggStatsService.getSggStats(sidoName, searchCondition);
            return ResponseEntity.ok("시군구별 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/sido-stats")
    public ResponseEntity<String> getSidoStats(
            @RequestParam String itemCode,
            @RequestParam String dataGubun,
            @RequestParam String searchCondition) {
        try {
            sidoStatsService.getSidoStats(itemCode, dataGubun, searchCondition);
            return ResponseEntity.ok("시도별 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }
}
