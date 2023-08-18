package com.shy_polarbear.server.domain.quiz.dto;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import lombok.Builder;

@Builder
public record MultipleChoiceQuizScoreResponse(
        Long quizId,
        Long correctAnswerId,
        String explanation,
        boolean isCorrect,
        int point
) {
    public static MultipleChoiceQuizScoreResponse of(
            MultipleChoiceQuiz quiz,
            Long correctAnswerId,
            boolean isCorrect,
            int pointValue
    ) {
        return MultipleChoiceQuizScoreResponse.builder()
                .quizId(quiz.getId())
                .correctAnswerId(correctAnswerId)
                .explanation(quiz.getExplanation())
                .isCorrect(isCorrect)
                .point(pointValue)
                .build();
    }
}

