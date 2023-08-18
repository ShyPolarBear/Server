package com.shy_polarbear.server.domain.quiz.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record OXQuizScoreRequest(
        @NotBlank @Size(max = 1)
        String answer
) {
}
