package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsApiResponse;

/**
 * API 통신 인터페이스 <br/>
 * 외부 API와 통신하는 메서드들을 선언
 */
public interface ArpltnStatsApi {
    /**
     * 특정 측정소의 특정 기간 일 평균 대기 오염 통계 데이터를 조회
     * @param msrstnName 측정소명
     * @param inqBginDt 조회 시작일자 (YYYYMMDD)
     * @param inqEndDt 조회 종료일자 (YYYYMMDD)
     * @return ArpltnStatsApiResponse API 응답 객체
     */
    ArpltnStatsApiResponse getArpltnStats(String msrstnName, String inqBginDt, String inqEndDt);
}
