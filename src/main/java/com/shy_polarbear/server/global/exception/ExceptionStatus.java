package com.shy_polarbear.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {

<<<<<<< HEAD
    NICKNAME_DUPLICATION(409, 1101, "중복되는 닉네임입니다."),
    INVALID_TOKEN(401, 1001, "유효하지 않은 토큰입니다."),
    NOT_FOUND_USER(404, 1100, "존재하지 않는 회원입니다."),
    NOT_FOUND_REFRESH_TOKEN(400, 1002, "잘못된 refresh token입니다."),
    SIGNUP_TOKEN_ERROR(400, 1005, "잘못된 sign token입니다."),

    NOT_FOUND_COMMENT(404, 2100, "해당 댓글이 존재하지 않습니다.");
=======
    //공통
    CLIENT_ERROR(400, 9990, "클라이언트 오류"),
    SERVER_ERROR(500, 9991, "서버 오류"),
>>>>>>> origin/develop


    //로그인,회원가입
    UNAUTHORIZED_USER(401, 1000, "로그인이 필요합니다."),
    INVALID_ACCESS_TOKEN(401, 1001, "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(401, 1002, "유효하지 않은 refresh token입니다."),
    USER_ALREADY_EXISTS(400, 1004, "이미 가입된 유저입니다."),
    NEED_TO_JOIN(400, 1006, "회원가입이 필요합니다."),
    INVALID_KAKAO_TOKEN(400, 1007, "유효하지 않은 카카오 token입니다."),


    //회원
    NICKNAME_DUPLICATION(409, 1101, "중복되는 닉네임입니다."),
    NOT_FOUND_USER(404, 1100, "존재하지 않는 회원입니다.");

    private final int httpCode;
    private final int customErrorCode;
    private final String message;


}
