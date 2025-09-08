package dev.soon.feynman.arpltnCallBack.dao;

import dev.soon.feynman.arpltnCallBack.dto.DistrictData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GetDistrict {
    DistrictData getDistrictDataByCode(String districtCode);
}
