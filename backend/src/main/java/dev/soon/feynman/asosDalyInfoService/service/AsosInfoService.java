package dev.soon.feynman.asosDalyInfoService.service;

public interface AsosInfoService {
    /**
     * ASOS 자료 조회 및 저장
     * @param stnIds    종관기상관측 지점 번호
     * @param startDt   조회 기간 시작일(YYYYMMDD)
     * @param endDt     조회 기간 종료일(YYYYMMDD) (전일(D-1)까지 제공)
     */
    void getDailyStats(String stnIds, String startDt, String endDt);
}
