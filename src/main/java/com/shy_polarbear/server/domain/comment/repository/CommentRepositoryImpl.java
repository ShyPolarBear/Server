package com.shy_polarbear.server.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.QComment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.shy_polarbear.server.domain.comment.model.QComment.*;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Comment findBestComment(Feed feed) {
        JPAQuery<Comment> query = queryFactory
                .selectFrom(comment)
                .where(checkFeed(feed))
                .orderBy(
                        comment.commentLikes.size().desc(),
                        comment.createdDate.desc()
                )
                .limit(1);
        return query.fetchOne();
    }

    private static BooleanExpression checkFeed(Feed feed) {
        return comment.feed.id.eq(feed.getId());
    }
}
