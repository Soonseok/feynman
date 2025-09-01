package dev.soon.feynman.arpltnStatsSvc.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 시군구별 실시간 DTO <br/>
 * API 응답을 그대로 매핑
 */
@Data
public class SggStatsApiResponse {
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
        private String sidoName;
        private String cityName;
        private String cityNameEng;
        private String so2Value;
        private String coValue;
        private String o3Value;
        private String no2Value;
        private String pm10Value;
        private String pm25Value;
        private String khaiValue;
    }
}