package com.shy_polarbear.server.domain.quiz.dto.response;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoice;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import lombok.Builder;

@Builder
public record MultipleChoiceQuizScoreResponse(
        Long quizId,
        String correctAnswer,
        String explanation,
        boolean isCorrect,
        int point
) {
    public static final String ANSWER_FORMAT = "%s번 : %s";

    public static MultipleChoiceQuizScoreResponse of(
            MultipleChoiceQuiz quiz,
            int sequence,
            MultipleChoice answer,
            boolean isCorrect,
            int pointValue
    ) {
        return MultipleChoiceQuizScoreResponse.builder()
                .quizId(quiz.getId())
                .correctAnswer(String.format(ANSWER_FORMAT, sequence, answer.getContent()))
                .explanation(quiz.getExplanation())
                .isCorrect(isCorrect)
                .point(pointValue)
                .build();
    }

    public static MultipleChoiceQuizScoreResponse ofTimeout(
            MultipleChoiceQuiz quiz,
            int sequence,
            MultipleChoice answer,
            int pointValue
    ) {
        return MultipleChoiceQuizScoreResponse.builder()
                .quizId(quiz.getId())
                .correctAnswer(String.format(ANSWER_FORMAT, sequence, answer.getContent()))
                .explanation(quiz.getExplanation())
                .isCorrect(false)
                .point(pointValue)
                .build();
    }
}

