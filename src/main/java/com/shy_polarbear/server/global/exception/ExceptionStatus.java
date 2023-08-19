package com.shy_polarbear.server.global.exception;


import lombok.Getter;

@Getter
public enum ExceptionStatus {

    //공통
    CLIENT_ERROR(400, 9990, "클라이언트 오류"),
    SERVER_ERROR(500, 9991, "서버 오류"),
    INVALID_INPUT_VALUE(400, 9000, "올바르지 않은 입력값입니다."),

    //로그인,회원가입
    UNAUTHORIZED_USER(401, 1000, "로그인이 필요합니다."),
    INVALID_ACCESS_TOKEN(401, 1001, "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(401, 1002, "유효하지 않은 refresh token입니다."),
    USER_ALREADY_EXISTS(400, 1004, "이미 가입된 유저입니다."),
    NEED_TO_JOIN(400, 1006, "회원가입이 필요합니다."),
    INVALID_KAKAO_TOKEN(400, 1007, "유효하지 않은 카카오 token입니다."),


    //회원
    NICKNAME_DUPLICATION(409, 1101, "중복되는 닉네임입니다."),
    NOT_FOUND_USER(404, 1100, "존재하지 않는 회원입니다."),

    //이미지
    FAIL_UPLOAD_IMAGES(400, 7000, "이미지 업로드 실패했습니다."),
    INVALID_IMAGE_TYPE(400, 7001, "이미지 타입이 올바르지 않습니다."),
    FAIL_DELETE_IMAGES(400, 7030, "이미지 삭제 실패했습니다."),

    //피드
    NOT_FOUND_FEED(404, 2001, "존재하지 않는 피드입니다."),
    NOT_MY_FEED(400, 2002, "본인의 피드가 아닙니다.");

    private final int httpCode;
    private final int customErrorCode;
    private final String message;

    ExceptionStatus(int httpCode, int customErrorCode, String message) {
        this.httpCode = httpCode;
        this.customErrorCode = customErrorCode;
        this.message = message;
    }
}
