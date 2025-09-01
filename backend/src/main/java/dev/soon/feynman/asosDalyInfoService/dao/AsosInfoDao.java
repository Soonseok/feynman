package dev.soon.feynman.asosDalyInfoService.dao;

import dev.soon.feynman.asosDalyInfoService.dto.AsosInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AsosInfoDao {
    List<AsosInfoResponse> getAllAsosInfoResponse();

    int countAllAsosInfoResponse();

    int insertAsosInfoResponse(@Param("asosInfoResponse") AsosInfoResponse asosInfoResponse);
}
