package com.shy_polarbear.server.global.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

@Builder
@Getter
public class PageResponse<T> {  // 총 개수가 필요한 클라이언트에 대한 응답 DTO
    long count;
    boolean isLast;
    List<T> content;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .count(page.getTotalElements())
                .isLast(page.isLast())
                .content(page.getContent())
                .build();
    }

    public static <T> PageResponse<T> of(Slice<T> slice, long totalElements) {  // Slice + 카운트 쿼리
        return PageResponse.<T>builder()
                .count(totalElements)
                .isLast(slice.isLast())
                .content(slice.getContent())
                .build();
    }
}