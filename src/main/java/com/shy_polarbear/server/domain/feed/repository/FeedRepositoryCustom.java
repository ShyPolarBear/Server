package com.shy_polarbear.server.domain.feed.repository;

import com.shy_polarbear.server.domain.feed.model.Feed;
import org.springframework.data.domain.Slice;

public interface FeedRepositoryCustom {

    Slice<Feed> findRecentFeeds(long lastFeedId, int limit);

    Slice<Feed> findBestFeeds(String cursor, int minFeedLikeCount, int limit);

    Slice<Feed> findRecentBestFeeds(String cursor, String earliestDate, int limit);

    Slice<Feed> findAllFeedsByUserComment(Long lastCommentId, int limit, long userId);

    Slice<Feed> findUserFeeds(Long lastFeedId, int limit, Long userId);
}
