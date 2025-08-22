package dev.soon.feynman.arpltnStatsSvc.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ArpltnStatsResponse {
    private String stationName;
    private LocalDate measurementDate;
    // 데이터 값 크기는 모두 10
    private Double so2;         // 아황산가스 평균농도(단위:ppm)
    private Double co;          // 일산화탄소 평균농도(단위:ppm)
    private Double o3;          // 오존 평균농도(단위:ppm)
    private Double no2;         // 이산화질소 평균농도(단위:ppm)
    private Double pm10;        // 미세먼지 평균농도(단위:ug/m3)
    private Double pm25;        // 초미세먼지 평균농도(단위:ug/m3)
}
