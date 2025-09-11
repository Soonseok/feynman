package dev.soon.feynman.arpltnCallBack.dao;

import dev.soon.feynman.arpltnCallBack.dto.DistrictData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GetDistrict {
    DistrictData getDistrictDataByCode(String districtCode);

    List<String> stationIncludedDistrictCodeList();
}
