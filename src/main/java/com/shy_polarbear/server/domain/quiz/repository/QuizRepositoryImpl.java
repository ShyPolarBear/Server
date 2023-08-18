package com.shy_polarbear.server.domain.quiz.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.quiz.model.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.shy_polarbear.server.domain.quiz.model.QQuiz.quiz;
import static com.shy_polarbear.server.domain.quiz.model.QUserQuiz.userQuiz;

@Repository
@RequiredArgsConstructor
public class QuizRepositoryImpl implements QuizRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    // 현재 유저가 아직 풀지 않은 OX 퀴즈, 객관식 퀴즈. 최신순으로 정렬 -> 1개만
    // UserQuiz INNER 조인 -> WHERE UserQuiz
    // JOIN (UserQuiz.Quiz, Quiz)

    public Optional<Quiz> findRecentQuizNotYetSolvedByUser(Long userId) {
        JPAQuery<Quiz> query = queryFactory.selectFrom(quiz)
                .where(quiz.id.notIn(JPAExpressions.select(userQuiz.quiz.id)    // 해당 유저가 풀지 않은
                        .from(userQuiz)
                        .where(userQuiz.user.id.eq(userId)))
                )
                .orderBy(quiz.id.desc());    // 최신순

        return Optional.ofNullable(query.fetchFirst());
    }
}
