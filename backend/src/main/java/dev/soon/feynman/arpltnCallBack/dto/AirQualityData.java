package dev.soon.feynman.arpltnCallBack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 이 DTO는 실제 데이터를 담음 (예: 서울특별시의 미세먼지 값)<br/>
 */
@Getter
@Builder
@AllArgsConstructor
public class AirQualityData {
    private String sido;
    private String date;
    private String dataType;
    private Double pm10_value;
//    private Double so2_value;
//    private Double o3_value;
//    private Double co_value;
//    private Double no2_value;
//    private Double pm25_value;
//    private Double khai_value;
}
