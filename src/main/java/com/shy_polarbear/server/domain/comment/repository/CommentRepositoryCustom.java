package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface CommentRepositoryCustom {

    Optional<Comment> findBestComment(Feed feed);

    Slice<Comment> findAllParentComment(long currentUserId, long feedId, Long cursorId, int limit);

    Slice<Comment> findRecentUserCommentsInFeed(Long lastCommentId, Integer limit, Long userId);
}
