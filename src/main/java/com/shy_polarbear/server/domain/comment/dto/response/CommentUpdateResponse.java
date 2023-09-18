package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import lombok.Builder;

@Builder
public record CommentUpdateResponse(
        long commentId
) {
    public static CommentUpdateResponse of(Comment comment) {
        return CommentUpdateResponse.builder()
                .commentId(comment.getId())
                .build();
    }
}

