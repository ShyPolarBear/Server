package com.shy_polarbear.server.domain.quiz.dto.response;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.Quiz;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
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
    public static QuizCardResponse of(Quiz quiz) {
        return QuizCardResponse.builder()
                .quizId(quiz.getId())
                .type(quiz.getType().getValue())
                .question(quiz.getQuestion())
                .time(BusinessLogicConstants.MULTIPLE_CHOICE_QUIZ_TIME_LIMIT)
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


    private static int resolveTimeLimitFromDynamicQuizType(Quiz quiz) {
        if (quiz.getClass().equals(MultipleChoiceQuiz.class)) {
            return BusinessLogicConstants.MULTIPLE_CHOICE_QUIZ_TIME_LIMIT;
        }

        return BusinessLogicConstants.OX_QUIZ_TIME_LIMIT;
    }
}

