package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentPageResponse {

    private Integer count;

    private Boolean isLast;

    private List<AllCommentsInFeed> allCommentsInFeed;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    private static class AllCommentsInFeed {
        private Long commentId;

        private String author;

        private String authorProfileImage;

        private String content;

        private Long likeCount;

        private Boolean isAuthor;

        private Boolean isLike;

        private LocalDateTime createdDate;

        private Boolean isDeleted;
        private static AllCommentsInFeed from(Comment comment, User currentUser) {
            Long commentId = comment.getId();
            String author = comment.getAuthor().getNickName();
            String authorProfileImage = comment.getAuthor().getProfileImage();
            String content = comment.getContent();
            Long likeCount = comment.getLikesCount();
            Boolean isAuthor = comment.isAuthor(currentUser);
            Boolean isLike = comment.isLike(currentUser);
            LocalDateTime createdDate = comment.getCreatedAt();
            Boolean isDeleted = comment.isDeleted();

            return new AllCommentsInFeed(commentId, author, authorProfileImage, content, likeCount,
                    isAuthor, isLike, createdDate, isDeleted);
        }
    }

    @Builder
    public CommentPageResponse(Boolean isLast, List<Comment> commentList, User currentUser){
        this.count = commentList.size();
        this.isLast = isLast;
        this.allCommentsInFeed = commentList.stream()
                .map((comment -> AllCommentsInFeed.from(comment, currentUser)))
                .collect(Collectors.toList());
    }
}