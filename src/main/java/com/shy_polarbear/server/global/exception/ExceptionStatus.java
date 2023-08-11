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


    //댓글
    NOT_FOUND_COMMENT(404, 2100, "해당 댓글이 존재하지 않습니다."),
    NOT_ALLOWED_SLEF_REPORT(400, 2101, "자신의 댓글을 신고할 수 없습니다."),
    COMMENT_REPORT_DUPLICATION(400, 2103, "이미 신고한 댓글입니다."),
    NOT_MY_COMMENT_UPDATE(400, 2104, "자신이 작성한 댓글만 수정할 수 있습니다."),
    NOT_MY_COMMENT_DELETE(400, 2104, "자신이 작성한 댓글만 삭제할 수 있습니다."),
    ALREADY_LIKED_COMMENT(400, 2105, "이미 좋아요를 눌렀습니다."),


    // 피드
    NOT_FOUND_FEED(404, 2001, "해당 피드가 존재하기 않습니다."),

    //공통
    CLIENT_ERROR(400, 9990, "클라이언트 오류"),
    SERVER_ERROR(500, 9991, "서버 오류"),


    //로그인,회원가입
    UNAUTHORIZED_USER(401, 1000, "로그인이 필요합니다."),
    INVALID_ACCESS_TOKEN(401, 1001, "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(401, 1002, "유효하지 않은 refresh token입니다."),
    USER_ALREADY_EXISTS(400, 1004, "이미 가입된 유저입니다."),
    NEED_TO_JOIN(400, 1006, "회원가입이 필요합니다."),
    INVALID_KAKAO_TOKEN(400, 1007, "유효하지 않은 카카오 token입니다."),



    //이미지
    FAIL_UPLOAD_IMAGES(400, 7000, "이미지 업로드 실패했습니다."),
    INVALID_IMAGE_TYPE(400, 7001, "이미지 타입이 올바르지 않습니다."),
    INVALID_IMAGE_COUNT(400, 7002, "올바르지 않은 이미지 개수입니다."),
    FAIL_DELETE_IMAGES(400, 7030, "이미지 삭제 실패했습니다."),;

    private final int httpCode;
    private final int customErrorCode;
    private final String message;


}
