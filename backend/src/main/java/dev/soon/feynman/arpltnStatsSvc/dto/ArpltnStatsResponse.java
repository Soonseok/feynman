package dev.soon.feynman.arpltnStatsSvc.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 표준화 DTO <br/>
 * API 응답을 그대로 매핑하는 DTO를 각 Service 메서드가 이 DTO 형태로 바꿔줌<br/>
 * 이 형태를 DB에 기록
 */
@Getter
@Builder
public class ArpltnStatsResponse {
    private String stationName;
    private String stationCode;
    private String dataType;    // 측정 방식. 실시간, 일평균, 월평균 등
    private LocalDateTime measurementDateTime;
    // 데이터 값 크기는 모두 10
    private Double so2;         // 아황산가스 평균농도(단위:ppm)
    private Double co;          // 일산화탄소 평균농도(단위:ppm)
    private Double o3;          // 오존 평균농도(단위:ppm)
    private Double no2;         // 이산화질소 평균농도(단위:ppm)
    private Double pm10;        // 미세먼지 평균농도(단위:ug/m3)
    private Double pm25;        // 초미세먼지 평균농도(단위:ug/m3)
    private Double khai;        // 통합대기환경지수 시군구별 API에만 존재
}
