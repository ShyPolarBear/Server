package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.entity.MultipleChoiceQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultipleChoiceQuizRepository extends JpaRepository<MultipleChoiceQuiz, Long> {
}
