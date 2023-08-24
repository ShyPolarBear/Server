package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuizRepository extends JpaRepository<UserQuiz, Long>, UserQuizRepositoryCustom {
    // deprecated
    Optional<UserQuiz> findFirstByCreatedDateStartingWithAndUserIdOrderByCreatedDateDesc(String localDateTime, Long userId);
}
