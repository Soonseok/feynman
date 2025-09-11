package dev.soon.feynman.arpltnCallBack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TotalArpltnResponseDto {
    private String stationName;
    private String stationCode;
    private AirQualityData airQualityData;
}
