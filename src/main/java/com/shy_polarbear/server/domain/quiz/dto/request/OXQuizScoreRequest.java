package com.shy_polarbear.server.domain.quiz.dto.request;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record OXQuizScoreRequest(
        @NotBlank @Size(max = 1)
        String answer
) {
        @Builder
        public OXQuizScoreRequest {
        }
}
