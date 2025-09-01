package dev.soon.feynman.asosDalyInfoService.api;

import dev.soon.feynman.asosDalyInfoService.dto.AsosDalyInfoApiResponse;

/**
 * API 통신 인터페이스 <br/>
 * 외부 API와 통신하는 메서드들을 선언
 */
public interface AsosInfoApiClient {
    /**
     * 조회조건(지점 번호, 시간)으로 종관기상관측일자료 데이터(지점 번호, 온도, 습도 등)를 조회
     * @param stnIds    종관기상관측 지점 번호
     * @param startDt   조회 기간 시작일(YYYYMMDD)
     * @param endDt     조회 기간 종료일(YYYYMMDD) (전일(D-1)까지 제공)
     * @return  AsosDalyInfoApiResponse API 응답 객체
     */
    AsosDalyInfoApiResponse callApi(String stnIds, String startDt, String endDt, int numOfRows, int pageNo);
}
