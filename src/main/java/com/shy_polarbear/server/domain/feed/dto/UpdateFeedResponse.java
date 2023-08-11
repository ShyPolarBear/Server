package com.shy_polarbear.server.domain.feed.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateFeedResponse {
    private Long feedId;
    private Long code;
    private String message;

    @Builder
    private UpdateFeedResponse(Long feedId, Long code, String message) {
        this.feedId = feedId;
        this.code = code;
        this.message = message;
    }

//    private UpdateFeedResponse(Long feedId) {
//        this.feedId = feedId;
//    }
}
