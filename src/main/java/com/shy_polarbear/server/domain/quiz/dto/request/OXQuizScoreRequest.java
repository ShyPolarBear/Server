package com.shy_polarbear.server.domain.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OXQuizScoreRequest {
    @NotBlank
    @Size(max = 1)
    private String answer;
}
