package dev.soon.feynman.arpltnStatsSvc.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ArpltnStatsResponse {
    private Long id;
    private String stationName;
    private LocalDate measurementDate;
    private double so2;
    private double co;
    private double o3;
    private double no2;
    private double pm10;
    private double pm25;
}
