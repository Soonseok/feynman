package dev.soon.feynman.arpltnStatsSvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * API 응답을 그대로 매핑
 */
@Data
public class ArpltnStatsApiResponse {
    private String resultCode;
    private String resultMap;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;

    /**
     * 실제 데이터 목록을 담음
     */
    @JsonProperty("items")
    private List<Item> items;

    @Data
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
