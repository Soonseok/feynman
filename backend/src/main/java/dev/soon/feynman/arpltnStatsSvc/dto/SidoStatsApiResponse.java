package dev.soon.feynman.arpltnStatsSvc.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 시도별 실시간/일평균 DTO<br/>
 * API 응답을 그대로 매핑
 */
@Data
public class SidoStatsApiResponse {
    private Response response;

    @Data
    public static class Response {
        private Body body;
        private Header header;
    }

    @Data
    public static class Body {
        private int totalCount;
        private List<Item> items;
        private int pageNo;
        private int numOfRows;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    public static class Item {
        private String dataTime;
        private String itemCode;
        private String dataGubun;
        private String seoul;
        private String busan;
        private String daegu;
        private String incheon;
        private String gwangju;
        private String daejeon;
        private String ulsan;
        private String gyeonggi;
        private String gangwon;
        private String chungbuk;
        private String chungnam;
        private String jeonbuk;
        private String jeonnam;
        private String gyeongbuk;
        private String gyeongnam;
        private String jeju;
        private String sejong;
    }
}