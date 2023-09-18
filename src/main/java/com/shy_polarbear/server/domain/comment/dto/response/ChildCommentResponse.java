package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import lombok.Builder;

@Builder
public record ChildCommentResponse(
        Long commentId,
        String content,
        String createdDate,
        boolean isDeleted,
        String authorNickname,
        String authorProfileImage,
        boolean isAuthor,
        boolean isLike,
        long likeCount
) {
    public static ChildCommentResponse of(Comment comment, boolean isAuthor, boolean isLike) {
        return ChildCommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .isDeleted(!comment.getVisibility())
                .authorNickname(comment.getAuthor().getNickName())
                .authorProfileImage(comment.getAuthor().getProfileImage())
                .isAuthor(isAuthor)
                .isLike(isLike)
                .likeCount(comment.getCommentLikes().size())
                .build();
    }

}
