package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.global.common.util.EnumModel;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OXChoice implements EnumModel<String> {
    O("O"),X("X");

    private final String value;

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static OXChoice toEnum(String stringParam) {
        return switch (stringParam.toUpperCase()) {
            case "O" -> O;
            case "X" -> X;
            default -> throw new RuntimeException(ExceptionStatus.SERVER_ERROR.getMessage());
        };
    }
}
