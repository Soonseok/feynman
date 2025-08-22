package dev.soon.feynman.arpltnStatsSvc.dao;

import dev.soon.feynman.arpltnStatsSvc.dto.ArpltnStatsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArpltnStatsDao {
    List<ArpltnStatsResponse> getAllArpltnStatsResponse();

    int countAllArpltnStatsResponse();

    int insertArpltnStatsResponse(@Param("arpltnStatsResponse") ArpltnStatsResponse arpltnStatsResponse);
}
