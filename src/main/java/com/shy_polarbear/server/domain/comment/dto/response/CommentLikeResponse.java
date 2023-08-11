package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeResponse {

    private Long commentLikeId;

    public CommentLikeResponse(CommentLike commentLike){
        this.commentLikeId = commentLike.getId();
    }
}
