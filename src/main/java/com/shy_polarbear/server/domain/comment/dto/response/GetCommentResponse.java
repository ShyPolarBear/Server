package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetCommentResponse {


    private List<CommentInfo> comments;
    private Long lastCommentId;

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
