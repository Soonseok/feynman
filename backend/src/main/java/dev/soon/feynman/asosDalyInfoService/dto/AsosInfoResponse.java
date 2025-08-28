package dev.soon.feynman.asosDalyInfoService.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 표준화 DTO <br/>
 * API 응답을 그대로 매핑하는 DTO를 각 Service 메서드가 이 DTO 형태로 바꿔줌<br/>
 * 이 형태를 DB에 기록
 */
@Getter
@Builder
@ToString
public class AsosInfoResponse {

    private LocalDateTime measurementDateTime; // tm (일시)
    private String stationId; // stnId (지점 번호)
    private String stationName; // stnNm (지점명)

    private Double averageTemperature; // avgTa (평균 기온)
    private Double minimumTemperature; // minTa (최저 기온)
    private Double maximumTemperature; // maxTa (최고 기온)
    private Double dailyRainfall; // sumRn (일강수량)
    private Double averageWindSpeed; // avgWs (평균 풍속)
    private Double maximumWindSpeed; // maxWs (최대 풍속)
    private Double maximumInstantaneousWindSpeed; // maxInsWs (최대 순간 풍속)
    private Double averageHumidity; // avgRhm (평균 상대습도)
    private Double minimumHumidity; // minRhm (최소 상대습도)
    private Double averageSeaLevelPressure; // avgPs (평균 해면기압)
    private Double dailySunshineHours; // sumSsHr (합계 일조 시간)
    private Double totalGlobalSolarRadiation; // sumGsr (합계 일사)
    private Double averageCloudAmount; // avgTca (평균 전운량)
    private Double averageSoilTemperature5cm; // avgCm5Te (평균 5cm 지중온도)
    private Double averageSoilTemperature10cm; // avgCm10Te (평균 10cm 지중온도)
    private Double averageSoilTemperature20cm; // avgCm20Te (평균 20cm 지중온도)
    private Double averageSoilTemperature30cm; // avgCm30Te (평균 30cm 지중온도)
}
