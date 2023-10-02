package com.shy_polarbear.server.domain.feed.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import static com.shy_polarbear.server.domain.comment.model.QComment.comment;
import static com.shy_polarbear.server.domain.feed.model.QFeed.*;
import static com.shy_polarbear.server.domain.feed.model.QFeedImage.feedImage;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory queryFactory;

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
    public Slice<Feed> findBestFeeds(String cursor, int minFeedLikeCount, int limit) {
        //베스트 : 전체 기간 중 좋아요 개수가 50개 이상인 피드를 보여준다
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        feed.feedLikes.size().goe(minFeedLikeCount),
                        lessThanCustomCursor(cursor)
                )
                .orderBy(
                        feed.feedLikes.size().desc(),
                        feed.id.desc()
                )
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Feed> findRecentBestFeeds(String cursor, String earliestDate, int limit) {
        //최근 인기순 : 7일 내 작성된 피드를 좋아요 순으로 보여준다.
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        feed.createdDate.goe(earliestDate),
                        lessThanCustomCursor(cursor)
                )
                .orderBy(
                        feed.feedLikes.size().desc(),
                        feed.id.desc()
                )
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Feed> findUserFeeds(Long lastFeedId, int limit, Long userId) {
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .leftJoin(feed.feedImages, feedImage).fetchJoin()
                .where(
                        feed.author.id.eq(userId),
                        lessThanLastFeedId(lastFeedId)
                )
                .orderBy(feed.id.desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    private BooleanExpression lessThanLastFeedId(Long lastFeedId) {
        if (lastFeedId == null || lastFeedId == 0) return null;
        else return feed.id.lt(lastFeedId);
    }

    private BooleanExpression lessThanCustomCursor(String customCursor){
        if (customCursor == null) {
            return null;
        }
        return StringExpressions
                .lpad(feed.feedLikes.size().stringValue(), 10, '0')
                .concat(StringExpressions.lpad(feed.id.stringValue(), 19, '0'))
                .lt(customCursor);
    }
}
