package com.shy_polarbear.server.domain.feed.repository;


import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
    Slice<Feed> findByIdLessThanAndAuthorIdOrderByIdDesc(Long id, Long userId, Pageable pageable);

    Slice<Feed> findByCommentsAuthorIdAndCommentsIdLessThanOrderByCommentsIdDesc(Long userId, Long commentId, Pageable pageable);

    Slice<Feed> findByAuthorIdOrderByIdDesc(Long userId, PageRequest pageable);

    Slice<Feed> findByCommentsAuthorIdOrderByCommentsIdDesc(Long userId, PageRequest pageable);

}
