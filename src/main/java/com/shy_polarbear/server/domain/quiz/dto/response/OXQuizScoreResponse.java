package com.shy_polarbear.server.domain.quiz.dto.response;

import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import lombok.Builder;

@Builder
public record OXQuizScoreResponse(
        Long quizId,
        String correctAnswer,    // O, X
        String explanation,
        boolean isCorrect,
        int point
) {
    public static OXQuizScoreResponse of(OXQuiz oxQuiz, boolean isCorrect, int pointValue) {
        return OXQuizScoreResponse.builder()
                .quizId(oxQuiz.getId())
                .correctAnswer(oxQuiz.getAnswer().getValue())
                .explanation(oxQuiz.getExplanation())
                .isCorrect(isCorrect)
                .point(pointValue)
                .build();
    }

    public static OXQuizScoreResponse ofTimeout(OXQuiz oxQuiz, int pointValue) {
        return OXQuizScoreResponse.builder()
                .quizId(oxQuiz.getId())
                .correctAnswer(oxQuiz.getAnswer().getValue())
                .explanation(oxQuiz.getExplanation())
                .isCorrect(false)
                .point(pointValue)
                .build();
    }}
