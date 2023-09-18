package com.shy_polarbear.server.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentDeleteResponse(
        long commentId
) {
    public static CommentDeleteResponse of(long commentId) {
        return CommentDeleteResponse.builder()
                .commentId(commentId)
                .build();
    }
}
