package com.shy_polarbear.server.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.shy_polarbear.server.domain.feed.model.QFeedLike.*;

@Repository
@RequiredArgsConstructor
public class FeedLikeRepositoryImpl implements FeedLikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Long countFeedLikes(Long feedId) {
        JPAQuery<Long> query = queryFactory
                .select(feedLike.count())
                .from(feedLike)
                .where(feedLike.feed.id.eq(feedId));
        return query.fetchOne();
    }
}
