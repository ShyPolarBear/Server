package com.shy_polarbear.server.domain.feed.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.shy_polarbear.server.domain.feed.model.QFeed.*;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public Slice<Feed> findRecentFeeds(Long lastFeedId, int limit) {
        //최신순
        //SELECT f
        //FROM feed
        //where id<lastFeedId
        //ORDER BY id DESC
        //LIMIT ?
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(checkLastFeedId(lastFeedId))
                .orderBy(feed.id.desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Feed> findBestFeeds(Long lastFeedId, int minFeedLikeCount, int limit) {
        //베스트 (전체 기간 중 좋아요 개수가 50개 이상인 피드를 보여준다)
        //SELECT f
        //FROM feed
        //where id<lastFeedId
        //and 좋아요개수 >= 50
        //ORDER BY 좋아요개수 DESC
        //LIMIT ?

        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        checkLastFeedId(lastFeedId),
                        feed.feedLikes.size().goe(minFeedLikeCount)
                )
                .orderBy(feed.feedLikes.size().desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Feed> findRecentBestFeeds(Long lastFeedId, String earliestDate, int limit) {
        //SELECT f
        //FROM feed
        //where id<lastFeedId
        //and 작성시점>=현재-7일전
        //ORDER BY 좋아요개수 DESC
        //LIMIT ?

        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        checkLastFeedId(lastFeedId),
                        feed.createdDate.goe(earliestDate)
                )
                .orderBy(feed.feedLikes.size().desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    private BooleanExpression checkLastFeedId(Long lastFeedId) {
        if (lastFeedId == null || lastFeedId == 0) return null;
        else return feed.id.lt(lastFeedId);
    }
}
