package com.shy_polarbear.server.domain.quiz.dto.response;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.Quiz;
import java.util.List;
import lombok.Builder;

@Builder
public record QuizCardResponse(
        long quizId,
        String type,
        String question,
        int time,
        List<MultipleChoiceResponse> choices    // nullable
) {
    public static QuizCardResponse from(Quiz quiz) {
        return QuizCardResponse.builder()
                .quizId(quiz.getId())
                .type(quiz.getType().getValue())
                .question(quiz.getQuestion())
                .time(quiz.getQuizTimeLimit())
                .choices(resolveChoicesFromDynamicQuizType(quiz))
                .build();
    }

    private static List<MultipleChoiceResponse> resolveChoicesFromDynamicQuizType(Quiz quiz) {
        if (quiz.getClass().equals(MultipleChoiceQuiz.class)) {
            return ((MultipleChoiceQuiz) quiz).getMultipleChoiceList().stream()
                    .map(MultipleChoiceResponse::of)
                    .toList();
        }

        return null;
    }

}

