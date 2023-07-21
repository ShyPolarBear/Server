package com.shy_polarbear.server.domain.user.dto.auth;

import lombok.Getter;

@Getter
public class LogoutResponse {
    private final String message = "로그아웃 처리되었습니다.";
}
