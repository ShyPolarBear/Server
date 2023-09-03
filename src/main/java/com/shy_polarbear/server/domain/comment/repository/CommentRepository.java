package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    boolean existsByUserAndComment(User user, Comment comment);
}
