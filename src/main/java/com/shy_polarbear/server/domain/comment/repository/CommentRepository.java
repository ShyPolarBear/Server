package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    // FeedId와 마지막 댓글ID를 사용하여 페이지네이션된 댓글 목록 가져오기
    @Query("SELECT c FROM Comment c WHERE c.feed.id = :feedId AND c.id < :lastCommentId ORDER BY c.id DESC")
    Slice<Comment> findCommentsByFeedIdWithCursor(Long feedId, Long lastCommentId, Pageable pageable);

    // parentId와 마지막 대댓글ID를 사용하여 페이제네이션된 대댓글 목록을 가져오기
    @Query("SELECT c FROM Comment c WHERE c.parent.id = :parentId AND c.id < :lastCommentId ORDER BY c.id DESC")
    Slice<Comment> findChildCommentsByParentIdWithCursor(Long parentId, Long lastCommentId, Pageable pageable);

    Slice<Comment> findByFeedIdOrderByCreatedAtDesc(Long feedId);
}
