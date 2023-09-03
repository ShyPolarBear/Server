package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;

public interface CommentRepositoryCustom {

    Comment findBestComment(Feed feed);
}
