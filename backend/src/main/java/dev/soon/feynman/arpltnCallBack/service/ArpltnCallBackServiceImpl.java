package dev.soon.feynman.arpltnCallBack.service;

import dev.soon.feynman.arpltnCallBack.dao.GetArpltnData;
import dev.soon.feynman.arpltnCallBack.dao.GetDistrict;
import dev.soon.feynman.arpltnCallBack.dto.AirQualityData;
import dev.soon.feynman.arpltnCallBack.dto.DistrictData;
import dev.soon.feynman.arpltnCallBack.dto.TotalArpltnResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArpltnCallBackServiceImpl implements ArpltnCallBackService {

    private final GetArpltnData getArpltnData;
    private final GetDistrict getDistrict;

    @Override
    public TotalArpltnResponseDto getAirPollutionDataByCode(String code) {
        DistrictData districtData = getDistrict.getDistrictDataByCode(code);
        if (districtData == null) {
            return null;
        }
        AirQualityData airQualityData = getArpltnData.getSpecificDataByCode(code);

        return TotalArpltnResponseDto.builder()
                .stationName(districtData.getDistrictName())
                .stationCode(districtData.getDistrictCode())
                .airQualityData(airQualityData)
                .build();
    }

    @Override
    public List<String> getStationIncludedDistrictCodeList() {
        return getDistrict.stationIncludedDistrictCodeList();
    }
}