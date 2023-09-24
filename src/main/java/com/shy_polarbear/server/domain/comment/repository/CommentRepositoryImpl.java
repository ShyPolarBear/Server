package com.shy_polarbear.server.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.shy_polarbear.server.domain.comment.model.QComment.comment;
import static com.shy_polarbear.server.domain.comment.model.QCommentLike.commentLike;
import static com.shy_polarbear.server.domain.feed.model.QFeed.feed;
import static com.shy_polarbear.server.domain.feed.model.QFeedImage.feedImage;
import static com.shy_polarbear.server.domain.user.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findBestComment(Feed feed) {
        JPAQuery<Comment> query = queryFactory
                .selectFrom(comment)
                .where(checkFeed(feed))
                .orderBy(
                        comment.commentLikes.size().desc(),
                        comment.createdDate.desc()
                );
        return Optional.ofNullable(query.fetchFirst());
    }

    // TODO: 현재는 child comment, comment_like 에 대해 default batch size (IN 절)로 쿼리 날리는 상태. VO 만들고, 부모와 자식 FETCH JOIN 해보자.
    @Override
    public Slice<Comment> findAllParentComment(long currentUserId, long feedId, Long cursorId, int limit) {
        JPAQuery<Comment> query = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.author, user).fetchJoin()
                .leftJoin(comment.feed, feed).fetchJoin()
                .join(comment.commentLikes, commentLike).fetchJoin()
                .where(eqFeedId(feedId).and(gtCursorId(cursorId))
                        .and(comment.parent.isNull()))
                .orderBy(comment.id.asc())  // 오래된 순
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public Slice<Comment> findRecentUserCommentsInFeed(Long lastCommentId, Integer limit, Long userId) {
        JPAQuery<Comment> query = queryFactory
                .selectFrom(comment)
                .join(comment.feed, feed).fetchJoin()
                .join(feed.author, user).fetchJoin()
                .leftJoin(feed.feedImages, feedImage).fetchJoin()
                .where(
                        findRecentUserCommentIdsInFeed(lastCommentId, userId)
                )
                .orderBy(comment.id.desc())
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));//fetch join -> 애플리케이션 단에서 limit

        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    private static BooleanExpression findRecentUserCommentIdsInFeed(Long lastCommentId, Long userId) {
        return comment.id.in(JPAExpressions
                .select(comment.id.max())
                .from(comment)
                .where(
                        comment.author.id.eq(userId),
                        ltCursorId(lastCommentId)
                )
                .groupBy(comment.feed.id));
    }

    private static BooleanExpression ltCursorId(Long lastCommentId) {
        if (lastCommentId == null || lastCommentId == 0) return null;
        else return comment.id.lt(lastCommentId);
    }

    private static BooleanExpression checkFeed(Feed feed) {
        return comment.feed.id.eq(feed.getId());
    }

    private static BooleanExpression inParentIds(List<Long> parentIdList) {
        return comment.parent.id.in(parentIdList);
    }

    private static BooleanExpression eqFeedId(long feedId) {
        return comment.feed.id.eq(feedId);
    }

    private static BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return comment.id.gt(cursorId);
    }
}
