package com.shy_polarbear.server.domain.feed.repository;


import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Slice<Feed> findByIdLessThanAndAuthorOrderByIdDesc(Long id, User user, Pageable pageable);

}
