package com.shy_polarbear.server.domain.feed.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;

import static com.shy_polarbear.server.domain.feed.model.QFeed.*;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public Slice<Feed> findRecentFeeds(Long lastFeedId, int limit) {
        //최신순
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(lessThanLastFeedId(lastFeedId))
                .orderBy(feed.id.desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Feed> findBestFeeds(Long lastFeedId, int minFeedLikeCount, int limit) {
        //베스트 : 전체 기간 중 좋아요 개수가 50개 이상인 피드를 보여준다
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        lessThanLastFeedId(lastFeedId),
                        feed.feedLikes.size().goe(minFeedLikeCount)
                )
                .orderBy(feed.feedLikes.size().desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Feed> findRecentBestFeeds(Long lastFeedId, String earliestDate, int limit) {
        //최근 인기순 : 7일 내 작성된 피드를 좋아요 순으로 보여준다.
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        lessThanLastFeedId(lastFeedId),
                        feed.createdDate.goe(earliestDate)
                )
                .orderBy(feed.feedLikes.size().desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    private BooleanExpression lessThanLastFeedId(Long lastFeedId) {
        if (lastFeedId == null || lastFeedId == 0) return null;
        else return feed.id.lt(lastFeedId);
    }
}
