package dev.soon.feynman.arpltnCallBack.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/arpltn-filtering")
public class ArpltnFiltering {

    /**
     * 하위 행정구역 개수를 이용한 평균 값 계산
     * select distinct(station_code) from air_pollution_stats where station_code like '41281%';
     * select round(avg(so2), 3) as so2_mean from air_pollution_stats
     * where station_code in (select distinct(station_code) from air_pollution_stats where station_code like '41281%')
     * order by measurement_date_time desc limit 2;
     * 위 두 쿼리는 해당 행정구역의 최신 데이터만 이용
     * 특정 기간, 특정 지역의 평균을 계산 해야 함
     */
    @PostMapping("/calculateAvg")
    public void calculateAverage(@RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 @RequestParam(required = false) String stationName,
                                 @RequestParam(required = false) String stationCode,
                                 @RequestParam(required = false) String measurementType){
        System.out.println("\nstartDate : "+ startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("stationName : "+ stationName);
        System.out.println("stationCode : "+ stationCode);
        System.out.println("measurementType : "+ measurementType + "\n");
    };

    @GetMapping("stationCode")
    public void calculateKhai(){};
}
