package com.shy_polarbear.server.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {

    private Long commentId;

    private String author;

    private String authorProfileImage;

    private String content;

    private Long likeCount;

    private Boolean isAuthor;

    private Boolean isLike;

    private String createdDate;

    @Builder
    public CommentResponse(Long commentId, String author, String authorProfileImage, String content, Long likeCount, Boolean isAuthor, Boolean isLike, String createdDate) {
        this.commentId = commentId;
        this.author = author;
        this.authorProfileImage = authorProfileImage;
        this.content = content;
        this.likeCount = likeCount;
        this.isAuthor = isAuthor;
        this.isLike = isLike;
        this.createdDate = createdDate;
    }
}
