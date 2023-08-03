package com.shy_polarbear.server.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentPageResponse {
    private List<GetCommentResponse.CommentInfo> comments;
    private boolean hasNextPage;
}
