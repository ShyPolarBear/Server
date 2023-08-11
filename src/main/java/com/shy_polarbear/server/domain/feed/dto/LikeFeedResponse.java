package com.shy_polarbear.server.domain.feed.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeFeedResponse {
    private Integer feedLikeId;
    private Long code;
    private String message;

    @Builder
    private LikeFeedResponse(Integer feedLikeId, Long code, String message) {
        this.feedLikeId = feedLikeId;
        this.code = code;
        this.message = message;
    }
}
