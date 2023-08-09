package com.shy_polarbear.server.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentPageRequest {

    private Long feedId;

    private int pageNumber;

    private int pageSize = 10;
}
