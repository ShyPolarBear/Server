package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateCommentResponse {

    private Long commentId;

    @Builder
    private UpdateCommentResponse(Long commentId) {
        this.commentId = commentId;
    }

    public static UpdateCommentResponse from(Comment comment){
        return UpdateCommentResponse.builder()
                .commentId(comment.getId())
                .build();
    }
}
