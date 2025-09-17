package dev.soon.feynman.arpltnCallBack.dto.search;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchConditionDto {
    private String field;
    private String operator; // 예: "gt", "lt", "eq", "like", "in"
    private Object value;
    private String logicalOperator; // 예: "and", "or"
    private List<SearchConditionDto> subConditions; // 재귀적 조건
}