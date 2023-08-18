package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.Quiz;

import java.util.Optional;

public interface QuizRepositoryCustom {
    Optional<Quiz> findRecentQuizNotYetSolvedByUser(Long userId);
}
