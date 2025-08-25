package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.SidoStatsApiResponse;

public interface SidoStatsApiClient {
    /**
     * 시도별 실시간 대기 오염 통계 데이터를 조회합니다.
     * @param itemCode 측정 항목 구분 (SO2, CO, O3, NO2, PM10, PM2.5)
     * @param dataGubun 요청 자료 구분 (HOUR, DAILY)
     * @param searchCondition 요청 데이터 기간 (WEEK, MONTH)
     * @return SidoStatsApiResponse API 응답 객체
     */
    SidoStatsApiResponse getSidoStats(String itemCode, String dataGubun, String searchCondition);
}