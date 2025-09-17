package dev.soon.feynman.arpltnCallBack.service;

import dev.soon.feynman.arpltnCallBack.dto.TotalArpltnResponseDto;
import java.util.List;

public interface ArpltnCallBackService {

    /**
     * 주어진 코드를 기준으로 미세먼지 측정소 데이터를 조회
     * @param code 측정소 코드
     * @return 조회된 데이터를 담은 DTO
     */
    TotalArpltnResponseDto getAirPollutionDataByCode(String code);

    /**
     * 측정소가 포함된 구역의 코드를 조회
     * @return 측정소 코드를 포함하는 구역의 코드 목록
     */
    List<String> getStationIncludedDistrictCodeList();
}