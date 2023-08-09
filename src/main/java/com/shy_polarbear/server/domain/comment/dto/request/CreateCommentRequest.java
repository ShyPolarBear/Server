package com.shy_polarbear.server.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

    private String content;

    private Long parentId;

    private CreateCommentRequest(Long parentId, String content) {
        this.parentId = parentId;
        this.content = content;
    }

    public static CreateCommentRequest create(Long parentId, String content) {
        return new CreateCommentRequest(parentId, content);
    }
}
