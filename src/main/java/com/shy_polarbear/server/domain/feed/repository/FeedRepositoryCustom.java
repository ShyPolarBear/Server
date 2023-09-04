package com.shy_polarbear.server.domain.feed.repository;

import com.shy_polarbear.server.domain.feed.model.Feed;
import org.springframework.data.domain.Slice;

public interface FeedRepositoryCustom {

    Slice<Feed> findRecentFeeds(Long lastFeedId, int limit);

    Slice<Feed> findBestFeeds(Long lastFeedId, int minFeedLikeCount, int limit);

    Slice<Feed> findRecentBestFeeds(Long lastFeedId, String earliestDate, int limit);

}
