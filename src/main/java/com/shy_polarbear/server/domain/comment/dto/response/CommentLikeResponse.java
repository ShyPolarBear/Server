package com.shy_polarbear.server.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentLikeResponse (
        boolean isLiked
){
    public static CommentLikeResponse of(boolean isLiked) {
        return CommentLikeResponse.builder()
                .isLiked(isLiked)
                .build();
    }
}
