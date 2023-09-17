package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import lombok.Builder;

@Builder
public record CommentCreateResponse(
        long commentId,
        Long parentId  // nullable
) {
    public static CommentCreateResponse ofChild(Comment comment) {
        return CommentCreateResponse.builder()
                .parentId(comment.getParent().getId())
                .commentId(comment.getId())
                .build();
    }

    public static CommentCreateResponse ofParent(Comment comment) {
        return CommentCreateResponse.builder()
                .commentId(comment.getId())
                .build();
    }
}

