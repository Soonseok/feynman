package dev.soon.feynman.arpltnStatsSvc.service;

/**
 * 서비스 인터페이스 <br/>
 * 모듈이 제공할 비즈니스 로직 명시
 */
public interface DailyStatsService {
    /**
     * 특정 측정소의 대기 오염 통계 데이터를 조회 및 저장
     * @param msrstnName 측정소명
     * @param inqBginDt 조회 시작일자 (YYYYMMDD)
     * @param inqEndDt 조회 종료일자 (YYYYMMDD)
     * @return
     */
    void getDailyStats(String msrstnName, String inqBginDt, String inqEndDt);
}
