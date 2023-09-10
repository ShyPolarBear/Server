package com.shy_polarbear.server.domain.feed.repository;

import com.shy_polarbear.server.domain.feed.model.Feed;
import org.springframework.data.domain.Slice;

public interface FeedRepositoryCustom {

    Slice<Feed> findRecentFeeds(Long lastFeedId, int limit);

    Slice<Feed> findBestFeeds(String cursor, int minFeedLikeCount, int limit);

    Slice<Feed> findRecentBestFeeds(String cursor, String earliestDate, int limit);

}
