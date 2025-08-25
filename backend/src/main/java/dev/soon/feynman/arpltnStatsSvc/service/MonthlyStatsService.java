package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;

import java.util.List;

public interface MonthlyStatsService {
    /**
     * 특정 측정소의 특정 기간 월 평균 대기 오염 데이터를 조회하고 반환합니다.
     * @param msrstnName 측정소명
     * @param inqBginMm 조회 시작월 (YYYYMM)
     * @param inqEndMm 조회 종료월 (YYYYMM)
     * @return ArpltnStatsResponse 리스트
     */
    List<ArpltnStatsResponse> getMonthlyStats(String msrstnName, String inqBginMm, String inqEndMm);
}
