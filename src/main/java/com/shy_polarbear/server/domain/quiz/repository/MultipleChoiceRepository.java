package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultipleChoiceRepository extends JpaRepository<MultipleChoice, Long> {
    List<MultipleChoice> findAllByMultipleChoiceQuizId(Long quizId);
}
