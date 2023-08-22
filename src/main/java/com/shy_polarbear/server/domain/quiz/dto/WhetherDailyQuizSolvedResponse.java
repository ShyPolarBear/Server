package com.shy_polarbear.server.domain.quiz.dto;

import lombok.Builder;

@Builder
public record WhetherDailyQuizSolvedResponse(
        Long quizId,    // nullable
        boolean isSolved
) {
    public static WhetherDailyQuizSolvedResponse of(Long quizId, boolean isSolved) {
        return WhetherDailyQuizSolvedResponse.builder()
                .quizId(quizId)
                .isSolved(isSolved)
                .build();
    }
}

