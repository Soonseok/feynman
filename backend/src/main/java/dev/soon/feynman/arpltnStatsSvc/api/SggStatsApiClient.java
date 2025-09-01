package dev.soon.feynman.arpltnStatsSvc.api;

import dev.soon.feynman.arpltnStatsSvc.dto.SggStatsApiResponse;

public interface SggStatsApiClient {
    /**
     * 시군구별 실시간 대기 오염 평균 데이터를 조회합니다.
     * @param sidoName 시도 이름 (예: "서울")
     * @param searchCondition 요청 데이터 기간 (DAILY)
     * @param numOfRows 한 페이지에 출력 되는 결과 수
     * @param pageNo 현재 페이지 번호
     * @return SggStatsApiResponse API 응답 객체
     */
    SggStatsApiResponse callApi(String sidoName, String searchCondition, int numOfRows, int pageNo);
}
