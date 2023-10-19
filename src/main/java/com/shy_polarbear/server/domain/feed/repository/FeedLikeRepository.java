package com.shy_polarbear.server.domain.feed.repository;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedLike;
import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedLikeRepository extends JpaRepository<FeedLike, Long>, FeedLikeRepositoryCustom {
    void deleteByUserIdAndFeedId(Long userId, Long feedId);
    boolean existsByUserIdAndFeedId(Long userId, Long feedId);

}
