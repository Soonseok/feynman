package dev.soon.feynman.arpltnStatsSvc.dao;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ArpltnStatsDao {
    List<ArpltnStatsResponse> getAllArpltnStatsResponse();

    int countAllArpltnStatsResponse();

    int insertArpltnStatsResponse(@Param("arpltnStatsResponse") ArpltnStatsResponse arpltnStatsResponse);

    void disableSafeUpdates();  // 안전 모드 비활성화
    void matchStationCode();    // station_code 매칭

    @Select("SELECT MAX(measurement_date_time) FROM air_pollution_stats WHERE station_name = #{stationName}")
    LocalDateTime findLastMeasurementDate(@Param("stationName") String stationName);
}
