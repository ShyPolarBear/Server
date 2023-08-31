package com.shy_polarbear.server.domain.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceQuizScoreRequest {
        @NotNull
        private Long answerId;
}
