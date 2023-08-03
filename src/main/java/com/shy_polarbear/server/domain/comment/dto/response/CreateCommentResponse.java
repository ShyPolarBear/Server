package com.shy_polarbear.server.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentResponse {

    private Long commentId;

    private Long parentId;


    public CreateCommentResponse(Long commentId, Long parentId) {
        this.commentId = commentId;
        this.parentId = parentId;
    }
}
