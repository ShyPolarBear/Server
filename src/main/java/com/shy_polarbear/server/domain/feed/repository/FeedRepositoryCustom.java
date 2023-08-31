package com.shy_polarbear.server.domain.feed.repository;

import com.shy_polarbear.server.domain.feed.model.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface FeedRepositoryCustom {

    Slice<Feed> findRecentFeeds(Long lastFeedId, int limit);

    Slice<Feed> findBestFeeds(Long lastFeedId, int minFeedLikeCount, int limit);

    Slice<Feed> findPopularFeedsWithinEarliestDate(Long lastFeedId, LocalDateTime earliestDate, int limit);

}
