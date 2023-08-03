package com.shy_polarbear.server.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentResponse {

    private Long commentId;

    private String author;

    private String authorProfileImage;

    private String content;

    private Long likeCount;

    private Boolean isAuthor;

    private Boolean isLike;

    private String createdDate;

    private String childComments;
}
