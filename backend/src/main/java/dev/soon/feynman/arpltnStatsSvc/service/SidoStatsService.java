package dev.soon.feynman.arpltnStatsSvc.service;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;

import java.util.List;

public interface SidoStatsService {
    /**
     * 시도별 실시간/일평균 대기 오염 데이터를 조회하고 반환합니다.
     * @param itemCode 측정 항목 구분 (SO2, CO, O3, NO2, PM10, PM2.5)
     * @param dataGubun 요청 자료 구분 (HOUR, DAILY)
     * @param searchCondition 요청 데이터 기간 (WEEK, MONTH)
     * @return ArpltnStatsResponse 리스트
     */
    List<ArpltnStatsResponse> getSidoStats(String itemCode, String dataGubun, String searchCondition);
}
