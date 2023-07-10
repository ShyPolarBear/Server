package com.shy_polarbear.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {

    NICKNAME_DUPLICATION(409, 1101, "중복되는 닉네임입니다."),
    INVALID_TOKEN(401, 1001, "유효하지 않은 토큰입니다.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;


}
