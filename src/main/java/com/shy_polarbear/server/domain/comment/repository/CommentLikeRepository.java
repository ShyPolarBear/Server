package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
