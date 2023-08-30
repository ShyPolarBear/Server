package com.shy_polarbear.server.domain.quiz.dto.request;

import lombok.Builder;

import javax.validation.constraints.NotNull;

public record MultipleChoiceQuizScoreRequest(
        @NotNull
        Long answerId
) {
        @Builder
        public MultipleChoiceQuizScoreRequest {
        }
}
