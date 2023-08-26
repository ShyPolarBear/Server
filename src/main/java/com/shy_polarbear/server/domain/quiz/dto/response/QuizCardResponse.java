package com.shy_polarbear.server.domain.quiz.dto.response;

import com.shy_polarbear.server.domain.quiz.dto.QuizType;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import lombok.Builder;

import java.util.List;

@Builder
public record QuizCardResponse(
        long quizId,
        String type,
        String question,
        int time,
        List<MultipleChoiceResponse> choices    // nullable
) {
    public static QuizCardResponse of(MultipleChoiceQuiz quiz) {
        return QuizCardResponse.builder()
                .quizId(quiz.getId())
                .type(QuizType.MULTIPLE_CHOICE.getValue())
                .question(quiz.getQuestion())
                .time(BusinessLogicConstants.MULTIPLE_CHOICE_QUIZ_TIME_LIMIT)
                .choices(quiz.getMultipleChoiceList().stream().map(MultipleChoiceResponse::of).toList())
                .build();
    }

    public static QuizCardResponse of(OXQuiz quiz) {
        return QuizCardResponse.builder()
                .quizId(quiz.getId())
                .type(QuizType.OX.getValue())
                .question(quiz.getQuestion())
                .time(BusinessLogicConstants.OX_QUIZ_TIME_LIMIT)
                .build();
    }
}

