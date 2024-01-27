package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.global.common.util.EnumModel;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QuizType implements EnumModel<String> {
    OX("OX", 17), MULTIPLE_CHOICE("MULTIPLE_CHOICE", 17);

    private final String value;
    private final int timeLimit;

    public static QuizType of(String param) {
        return Arrays.stream(QuizType.values())
                .filter(v -> v.getValue().equals(param.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new QuizException(ExceptionStatus.CLIENT_ERROR));
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }
}
