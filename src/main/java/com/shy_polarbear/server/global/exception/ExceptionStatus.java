package com.shy_polarbear.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {

    //로그인,회원가입
    UNAUTHORIZED_USER(401, 1000, "로그인이 필요합니다."),
    INVALID_ACCESS_TOKEN(401, 1001, "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(401, 1002, "유효하지 않은 refresh token입니다."),
    USER_ALREADY_EXISTS(400, 1004, "이미 가입된 유저입니다."),
    NEED_TO_JOIN(400, 1006, "회원가입이 필요합니다."),


    //회원
    NICKNAME_DUPLICATION(409, 1101, "중복되는 닉네임입니다."),
    NOT_FOUND_USER(404, 1100, "존재하지 않는 회원입니다.");

    private final int httpCode;
    private final int customErrorCode;
    private final String message;


}
