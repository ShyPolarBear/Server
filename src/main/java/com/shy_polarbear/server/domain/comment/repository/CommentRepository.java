package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentId(Long commentId);
}
