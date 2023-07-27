package com.shy_polarbear.server.global.auth.security;

import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    //유틸리티 클래스 : 인스턴화 방지를 위해 private 생성자
    private SecurityUtils() {
    }

    public static String getLoginUserProviderId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UserException(ExceptionStatus.NOT_FOUND_USER);
        }
        return authentication.getName();
    }
}
