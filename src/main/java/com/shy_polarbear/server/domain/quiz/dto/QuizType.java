package com.shy_polarbear.server.domain.quiz.dto;

import com.shy_polarbear.server.global.common.util.EnumModel;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QuizType implements EnumModel<String> {
    OX("OX"), MULTIPLE_CHOICE("MULTIPLE_CHOICE");

    private final String value;

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static QuizType toEnum(String stringParam) {
        return switch (stringParam.toUpperCase()) {
            case "OX" -> OX;
            case "MULTIPLE_CHOICE" -> MULTIPLE_CHOICE;

            default -> throw new RuntimeException(ExceptionStatus.SERVER_ERROR.getMessage());
        };
    }
}
