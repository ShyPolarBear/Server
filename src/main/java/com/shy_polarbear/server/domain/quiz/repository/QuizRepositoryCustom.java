package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.Quiz;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface QuizRepositoryCustom {
    Optional<Quiz> findRecentQuizNotYetSolvedByUser(Long userId);

    Slice<Quiz> findRandomQuizzesAlreadySolvedByUser(Long userId, int limit);

    Long countAllQuizzesAlreadySolvedByUser(Long userId);
}
