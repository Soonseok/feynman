package dev.soon.feynman.arpltnCallBack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 데이터를 감싸는 컨테이너 역할 (예: 성공/실패 여부, 총 데이터 개수)
 */
@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private int totalCount;
    private int dataSize;
    private T data;
}
