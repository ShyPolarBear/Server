package com.shy_polarbear.server.domain.quiz.dto.response;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoice;
import lombok.Builder;

@Builder
public record MultipleChoiceResponse(long id, String text) {
    public static MultipleChoiceResponse of(MultipleChoice multipleChoice) {
        return MultipleChoiceResponse.builder()
                .id(multipleChoice.getId())
                .text(multipleChoice.getContent())
                .build();
    }
}

