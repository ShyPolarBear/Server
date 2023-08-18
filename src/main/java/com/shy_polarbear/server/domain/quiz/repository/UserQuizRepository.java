package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
}
