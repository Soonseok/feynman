package dev.soon.feynman.arpltnCallBack.dao;

import dev.soon.feynman.arpltnCallBack.dto.AirQualityData;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityDataWithStationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GetArpltnData {

    AirQualityData getSpecificDataByCode(String districtCode);

    Integer getNumberOfSimpleSearchedData(Map<String, Object> map);

    List<AirQualityDataWithStationCode> getSimpleSearchedData(@Param("params") Map<String, Object> map,
                                                              @Param("startNum") int startNum,
                                                              @Param("pageSize") int pageSize);
}
