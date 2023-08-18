package com.shy_polarbear.server.domain.quiz.dto;

import javax.validation.constraints.NotNull;

public record MultipleChoiceQuizScoreRequest(
        @NotNull
        Long answerId
) {
}
