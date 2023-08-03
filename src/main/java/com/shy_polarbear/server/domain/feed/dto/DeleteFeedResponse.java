package com.shy_polarbear.server.domain.feed.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteFeedResponse {
    private Long feedId;
    private Long code;
    private String message;

    @Builder
//    private DeleteFeedResponse(Long feedId, Long code, String message) {
//        this.feedId = feedId;
//        this.code = code;
//        this.message = message;
//    }
    private DeleteFeedResponse(Long feedId) {
        this.feedId = feedId;
    }
}


