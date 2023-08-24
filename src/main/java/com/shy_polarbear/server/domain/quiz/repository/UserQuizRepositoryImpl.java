package com.shy_polarbear.server.domain.quiz.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.quiz.model.QUserQuiz;
import com.shy_polarbear.server.domain.quiz.model.UserQuiz;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.shy_polarbear.server.domain.quiz.model.QUserQuiz.userQuiz;

@RequiredArgsConstructor
public class UserQuizRepositoryImpl implements UserQuizRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<UserQuiz> findFirstSubmittedDailyQuizByUser(String localDateTime, Long userId) {
        JPAQuery<UserQuiz> query = queryFactory.selectFrom(userQuiz)
                .where(userQuiz.createdDate.startsWith(localDateTime).and(userQuiz.user.id.eq(userId)))
                .orderBy(userQuiz.createdDate.desc());

        return Optional.ofNullable(query.fetchFirst());
    }
}
