package com.shy_polarbear.server.domain.feed.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.shy_polarbear.server.domain.comment.model.QComment.comment;
import static com.shy_polarbear.server.domain.feed.model.QFeed.*;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Feed> findRecentFeeds(long lastFeedId, int limit) {
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
    public Slice<Feed> findAllFeedsByUserComment(Long lastCommentId, int limit, long userId) {
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .join(feed.comments, comment)
                .where(
                        comment.author.id.eq(userId),
                        lessThanLastCommentId(lastCommentId)
                )
                .orderBy(
                        comment.id.desc()
                );

        //피드 리스트를 순회하면서 새로운 리스트에 같은 id가 없다면 해당 피드를 넣는다.
        List<Feed> feedsByUserComment = new ArrayList<>();
        query.fetch().stream()
                .filter(feed -> removeDuplicateFeeds(feedsByUserComment, feed, limit))
                .forEach(feedsByUserComment::add);
        return CustomSliceExecutionUtils.getSlice(feedsByUserComment, limit);
    }

    @Override
    public Slice<Feed> findUserFeeds(Long lastFeedId, int limit, Long userId) {
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .where(
                        feed.author.id.eq(userId),
                        lessThanLastFeedId(lastFeedId)
                )
                .orderBy(feed.id.desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));
        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    private static boolean removeDuplicateFeeds(List<Feed> feedsByUserComment, Feed feed, int limit) {
        //만약 feedsByUserComment가 limit만큼 채워졌다면 더이상 채울 수 없다.
        if (feedsByUserComment.size() < limit) {
            return !feedsByUserComment.stream()
                    .anyMatch(existFeed  -> existFeed.getId() == feed.getId());
        }
        return false;
    }

    private BooleanExpression lessThanLastCommentId(Long lastCommentId) {
        if (lastCommentId == null || lastCommentId == 0) return null;
        else return comment.id.lt(lastCommentId);
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
