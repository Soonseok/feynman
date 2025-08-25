package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;

import java.util.List;

public interface SggStatsService {
    /**
     * 시군구별 실시간 대기 오염 데이터를 조회하고 반환합니다.
     * @param sidoName 시도 이름
     * @param searchCondition 요청 데이터 기간 (DAILY)
     * @return ArpltnStatsResponse 리스트
     */
    List<ArpltnStatsResponse> getSggStats(String sidoName, String searchCondition);
}
