package dev.soon.feynman.arpltnCallBack.dto.search;

import dev.soon.feynman.arpltnCallBack.dto.TotalArpltnResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaginatedArpltnResponseDto {
    private List<TotalArpltnResponseDto> arpltnResponseList;
}
