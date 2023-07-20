package com.shy_polarbear.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {

    NICKNAME_DUPLICATION(409, 1101, "중복되는 닉네임입니다."),
    INVALID_TOKEN(401, 1001, "유효하지 않은 토큰입니다."),
    NOT_FOUND_USER(404, 1100, "존재하지 않는 회원입니다."),
    NOT_FOUND_REFRESH_TOKEN(400, 1002, "잘못된 refresh token입니다."),
    SIGNUP_TOKEN_ERROR(400, 1005, "잘못된 sign token입니다."),

    NOT_FOUND_COMMENT(404, 2100, "해당 댓글이 존재하지 않습니다.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;


}
