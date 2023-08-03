package com.shy_polarbear.server.domain.feed.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReportFeedResponse {
    private Long code;
    private String message;

    @Builder
    private ReportFeedResponse(Long code, String message) {
        this.code = code;
        this.message = message;
    }
}
