package com.shy_polarbear.server.domain.quiz.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.quiz.model.Quiz;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.shy_polarbear.server.domain.quiz.model.QQuiz.quiz;
import static com.shy_polarbear.server.domain.quiz.model.QUserQuiz.userQuiz;

@Repository
@RequiredArgsConstructor
public class QuizRepositoryImpl implements QuizRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Quiz> findRecentQuizNotYetSolvedByUser(Long userId) {
        JPAQuery<Quiz> query = queryFactory.selectFrom(quiz)
                .where(quiz.id.notIn(findSolvedQuizIdsSubQuery(userId)))
                .orderBy(quiz.id.desc());    // 최신순

        return Optional.ofNullable(query.fetchFirst());
    }

    @Override
    public Slice<Quiz> findRecentQuizzesAlreadySolvedByUser(Long userId, int limit) {
        JPAQuery<Quiz> query = queryFactory.selectFrom(quiz)
                .where(quiz.id.in(findSolvedQuizIdsSubQuery(userId)))
                .orderBy(quiz.id.desc())   // 최신순
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    /**
     * Count Query
     **/
    @Override
    public Long countAllRecentQuizzesAlreadySolvedByUser(Long userId) {
        JPAQuery<Long> query = queryFactory.select(quiz.count()).from(quiz)
                .where(quiz.id.in(findSolvedQuizIdsSubQuery(userId)));

        return query.fetchOne();
    }

    /**
     * SubQuery
     **/
    private static JPQLQuery<Long> findSolvedQuizIdsSubQuery(Long userId) {// 유저가 푼 문제의 ids
        return JPAExpressions.select(userQuiz.quiz.id)
                .from(userQuiz)
                .where(userQuiz.user.id.eq(userId));
    }
}
