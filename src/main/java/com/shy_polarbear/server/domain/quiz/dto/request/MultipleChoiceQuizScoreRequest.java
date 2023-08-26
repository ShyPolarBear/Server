package com.shy_polarbear.server.domain.quiz.dto.request;

import javax.validation.constraints.NotNull;

public record MultipleChoiceQuizScoreRequest(
        @NotNull
        Long answerId
) {
}
