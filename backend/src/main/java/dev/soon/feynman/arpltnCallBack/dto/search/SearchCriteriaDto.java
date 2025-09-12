package dev.soon.feynman.arpltnCallBack.dto.search;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteriaDto {
    private Optional<String> startDate = Optional.empty();
    private Optional<String> endDate = Optional.empty();
    private Optional<String> stationName = Optional.empty();
    private Optional<String> stationCode = Optional.empty();
    private Optional<String> dataType = Optional.empty();
    private Optional<List<String>> measurementType = Optional.empty();
}