package com.shy_polarbear.server.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // 인증이 필요한 리소스에 인증되지 않은 사용자가 접근할 경우 401 에러
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(ExceptionStatus.UNAUTHORIZED_USER.getHttpCode());
        String body = objectMapper.writeValueAsString(
                ApiResponse.error(ExceptionStatus.UNAUTHORIZED_USER.getCustomErrorCode(), ExceptionStatus.UNAUTHORIZED_USER.getMessage())
        );
        response.getWriter().write(body);
    }
}
