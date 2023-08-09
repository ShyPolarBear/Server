package com.shy_polarbear.server.domain.comment.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private List<CommentInfo> comments = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    public static class CommentInfo {

        private Long id;

        private String author;

        private String authorProfileImage;

        private String content;

        private Long likeCount;

        private Boolean isAuthor;

        private Boolean isLike;

        private String createdDate;

        private List<CommentInfo> childComments;
    }
}
