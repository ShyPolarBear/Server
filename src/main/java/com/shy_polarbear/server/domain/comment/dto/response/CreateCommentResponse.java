package com.shy_polarbear.server.domain.comment.dto.response;

import lombok.Getter;

@Getter
public class CreateCommentResponse {

    private Long commentId;

    private Long parentId;

    public CreateCommentResponse(Long commentId, Long parentId) {
        this.commentId = commentId;
        this.parentId = parentId;
    }

}
