package com.shy_polarbear.server.domain.user.model;


import com.shy_polarbear.server.domain.quiz.model.OXChoice;
import com.shy_polarbear.server.global.common.util.EnumModel;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProviderType implements EnumModel<String> {
    KAKAO("Kakao");

    public final String value;

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ProviderType toEnum(String stringParam) {
        return switch (stringParam.toUpperCase()) {
            case "Kakao" -> KAKAO;
            default -> throw new RuntimeException(ExceptionStatus.SERVER_ERROR.getMessage());
        };
    }
}
