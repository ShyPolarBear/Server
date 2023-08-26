package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.UserQuiz;

import java.util.Optional;

public interface UserQuizRepositoryCustom {
    Optional<UserQuiz> findFirstSubmittedDailyQuizByUser(String localDateTime, Long userId);

}
