package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    Slice<Comment> findAllByFeedIdOrderByCreatedAt(Long feedId, Pageable pageable);

    List<Comment> findAllByFeedIdOrderByCreatedAt(Long feedId, Pageable pageable);

    List<Comment> findAllByFeedIdAndIdLessThanOrderByCreatedAtDesc(Long feedId, Long commentId, Pageable pageable);

    boolean existsByIdLessThan(Long commentId);
}
