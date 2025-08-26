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

    // 날짜 파싱 실패 시 현재 아이템은 일단 건너 뜀.
    // TODO: 실패한 데이터를 관리하고 재처리하는 시스템 구축

    /**
     * 	측정소별 일평균 정보 조회 <br/>
     * ex) http://localhost:8505/api/v1/arpltn-stats/daily-stats?msrstnName=강남구&inqBginDt=20250824&inqEndDt=20250825
     * @param msrstnName
     * @param inqBginDt
     * @param inqEndDt
     * @return
     */
    @GetMapping("/daily-stats")
    public ResponseEntity<String> getDailyStats(
            @RequestParam String msrstnName,
            @RequestParam String inqBginDt,
            @RequestParam String inqEndDt) {
        try {
            dailyStatsService.getDailyStats(msrstnName, inqBginDt, inqEndDt);
            return ResponseEntity.ok("일평균 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 측정소별 월평균 정보 조회 <br/>
     * ex) http://localhost:8505/api/v1/arpltn-stats/monthly-stats?msrstnName=강남구&inqBginMm=202506&inqEndMm=202507
     * @param msrstnName
     * @param inqBginMm
     * @param inqEndMm
     * @return
     */
    @GetMapping("/monthly-stats")
    public ResponseEntity<String> getMonthlyStats(
            @RequestParam String msrstnName,
            @RequestParam String inqBginMm,
            @RequestParam String inqEndMm) {
        try {
            monthlyStatsService.getMonthlyStats(msrstnName, inqBginMm, inqEndMm);
            return ResponseEntity.ok("월평균 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 시도별 평균정보 조회 <br/>
     * ex) http://localhost:8505/api/v1/arpltn-stats/sgg-stats?sidoName=서울&searchCondition=DAILY
     * @param sidoName - 시도 이름
     * @param searchCondition - 요청 데이터기간, HOUR, DAILY
     * @return
     */
    @GetMapping("/sgg-stats")
    public ResponseEntity<String> getSggStats(
            @RequestParam String sidoName,
            @RequestParam String searchCondition) {
        try {
            sggStatsService.getSggStats(sidoName, searchCondition);
            return ResponseEntity.ok("시군구별 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 시군구별 평균정보 조회 <br/>
     * ex) http://localhost:8505/api/v1/arpltn-stats/sido-stats?itemCode=SO2&dataGubun=HOUR&searchCondition=WEEK
     * @param itemCode - SO2, CO, O3, NO2, PM10, PM25
     * @param dataGubun - 시간평균: HOUR, 일평균: DAILY
     * @param searchCondition - 일주일: WEEK, 한달: MONTH
     * @return
     */
    @GetMapping("/sido-stats")
    public ResponseEntity<String> getSidoStats(
            @RequestParam String itemCode,
            @RequestParam String dataGubun,
            @RequestParam String searchCondition) {
        try {
            sidoStatsService.getSidoStats(itemCode, dataGubun, searchCondition);
            return ResponseEntity.ok("시도별 데이터 수집 및 DB 저장 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("데이터 수집 중 오류 발생: " + e.getMessage());
        }
    }
}
