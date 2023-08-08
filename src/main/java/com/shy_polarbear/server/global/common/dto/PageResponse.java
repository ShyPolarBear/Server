package com.shy_polarbear.server.global.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PageResponse<T> {
    Long totalElements;
    boolean last;
    List<T> content;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .totalElements(page.getTotalElements())
                .last(page.isLast())
                .content(page.getContent())
                .build();
    }

    public static <T> PageResponse<T> of(Slice<T> slice) {  // 클라이언트에서 totalElements 사용 안 하는 경우, 카운트 쿼리 최적화 가능
        return PageResponse.<T>builder()
                .last(slice.isLast())
                .content(slice.getContent())
                .build();
    }

    public static <T> PageResponse<T> of(Slice<T> slice, Long totalElements) {
        return PageResponse.<T>builder()
                .totalElements(totalElements)
                .last(slice.isLast())
                .content(slice.getContent())
                .build();
    }

    // 리스트를 자체 페이지 처리
    public static <T> PageResponse<T> of(Pageable pageable, List<T> list) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        List<T> content = new ArrayList<>();

        if (start > end) list.subList(end, list.size());
        else content = list.subList(start, end);

        Page<T> newPage = new PageImpl<>(
                content,
                pageable,
                list.size()
        );
        return PageResponse.of(newPage);
    }

    public static <T, O> PageResponse<T> of(Page<O> origin, List<T> list) {
        return PageResponse.<T>builder()
                .totalElements(origin.getTotalElements())
                .last(origin.isLast())
                .content(list)
                .build();
    }
}