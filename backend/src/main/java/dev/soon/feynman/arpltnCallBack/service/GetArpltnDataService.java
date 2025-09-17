package dev.soon.feynman.arpltnCallBack.service;

import dev.soon.feynman.arpltnCallBack.dto.search.PaginatedArpltnResponseDto;
import dev.soon.feynman.arpltnCallBack.dto.search.SearchCriteriaDto;

public interface GetArpltnDataService {

    /**
     * 주어진 검색 조건과 페이지 정보를 사용하여 대기오염 데이터를 검색
     * @param searchCriteriaDto 검색 조건을 담은 DTO
     * @param page 페이지 번호
     * @return 검색된 데이터를 포함하는 페이지네이션된 응답 DTO
     */
    PaginatedArpltnResponseDto searchAirPollutionData(SearchCriteriaDto searchCriteriaDto, int page);
}