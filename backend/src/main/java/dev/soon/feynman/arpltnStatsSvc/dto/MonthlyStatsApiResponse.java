package dev.soon.feynman.arpltnStatsSvc.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 측정소별 월평균 DTO<br/>
 * API 응답을 그대로 매핑
 */
@Data
public class MonthlyStatsApiResponse {
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
        private String msurMm;
        private String msrstnName;
        private String so2Value;
        private String coValue;
        private String o3Value;
        private String no2Value;
        private String pm10Value;
        private String pm25Value;
    }
}