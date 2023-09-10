package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;

import java.util.Optional;

public interface CommentRepositoryCustom {

    Optional<Comment> findBestComment(Feed feed);
}
