package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OXQuizRepository extends JpaRepository<OXQuiz, Long> {
}
