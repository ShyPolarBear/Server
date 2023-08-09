package com.shy_polarbear.server.global.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Builder
@Getter
public class NoCountPageResponse<T> {   // 무한 스크롤 && 총 개수가 필요없는 클라이언트에 대한 응답 DTO
    boolean isLast;
    List<T> content;

    public static <T> NoCountPageResponse<T> of(Slice<T> slice) {  // 오직 Slice. 카운트 쿼리 최적화
        return NoCountPageResponse.<T>builder()
                .isLast(slice.isLast())
                .content(slice.getContent())
                .build();
    }
}