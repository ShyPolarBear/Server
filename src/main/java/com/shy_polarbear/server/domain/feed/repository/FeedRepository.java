package com.shy_polarbear.server.domain.feed.repository;


import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
    Slice<Feed> findByIdLessThanAndAuthorOrderByIdDesc(Long id, User author, Pageable pageable);

    Slice<Feed> findByCommentsAuthorAndCommentsIdLessThanOrderByCommentsIdDesc(User author, Long commentId, Pageable pageable);

    Slice<Feed> findByAuthorOrderByIdDesc(User author, PageRequest pageable);

    Slice<Feed> findByCommentsAuthorOrderByCommentsIdDesc(User user, PageRequest pageable);

}
