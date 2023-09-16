package com.shy_polarbear.server.domain.comment.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import lombok.Builder;

import java.util.List;

@Builder
public record CommentResponse(
        Long commentId,
        String content,
        String createdDate,
        boolean isDeleted,
        String authorNickname,
        String authorProfileImage,
        boolean isAuthor,
        boolean isLike,
        long likeCount,
        List<ChildCommentResponse> childComments

) {
    public static CommentResponse of(Comment comment, boolean isAuthor, boolean isLike, List<Comment> childCommentList, Long userId) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .isDeleted(!comment.getVisibility())
                .authorNickname(comment.getAuthor().getNickName())
                .authorProfileImage(comment.getAuthor().getProfileImage())
                .isAuthor(isAuthor)
                .isLike(isLike)
                .likeCount(comment.getCommentLikes().size())
                .createdDate(comment.getCreatedDate())
                .isDeleted(comment.getVisibility())
                .childComments(childCommentList.stream().map(it -> ChildCommentResponse.of(it, it.isAuthor(userId), it.isLike(userId))).toList())
                .build();
    }
}

