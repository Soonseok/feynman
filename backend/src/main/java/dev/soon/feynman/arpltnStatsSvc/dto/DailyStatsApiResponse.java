package dev.soon.feynman.arpltnStatsSvc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 측정소별 일평균 DTO <br/>
 * API 응답을 그대로 매핑
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyStatsApiResponse {

    private Response response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private Integer totalCount;
        private Integer numOfRows;
        private Integer pageNo;

        /**
         * 실제 데이터 목록을 담음
         */
        @JsonProperty("items")
        private List<Item> items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String msurDt;
        private String msrstnName;
        private String so2Value;
        private String coValue;
        private String o3Value;
        private String no2Value;
        private String pm10Value;
        private String pm25Value;
    }
}
