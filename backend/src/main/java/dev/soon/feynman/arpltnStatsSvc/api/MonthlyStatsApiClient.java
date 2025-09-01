package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.MonthlyStatsApiResponse;

public interface MonthlyStatsApiClient {
    /**
     * 특정 측정소의 특정 기간 월 평균 대기 오염 통계 데이터를 조회합니다.
     * @param msrstnName 측정소명 (예: "강남구")
     * @param inqBginMm 조회 시작월 (YYYYMM)
     * @param inqEndMm 조회 종료월 (YYYYMM)
     * @param numOfRows 한 페이지에 출력 되는 결과 수
     * @param pageNo 현재 페이지 번호
     * @return MonthlyStatsApiResponse API 응답 객체
     */
    MonthlyStatsApiResponse callApi(String msrstnName, String inqBginMm, String inqEndMm, int numOfRows, int pageNo);
}