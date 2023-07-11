package com.shy_polarbear.server.domain.feed.repository;


import com.shy_polarbear.server.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
