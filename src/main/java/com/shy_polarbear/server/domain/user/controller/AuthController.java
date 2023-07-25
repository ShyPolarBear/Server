package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.config.jwt.JwtDto;
import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.dto.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.SocialLoginRequest;
import com.shy_polarbear.server.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtDto> loginOAuth(@RequestBody SocialLoginRequest socialLoginRequest) {
        return ApiResponse.success(authService.authLogin(socialLoginRequest));
    }

    @PostMapping("/join")
    public ApiResponse<JwtDto> join(@RequestBody JoinRequest joinRequest) {
        return ApiResponse.success(authService.join(joinRequest));
    }

    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return ApiResponse.success(authService.reissue(reissueRequest.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout() {
        return ApiResponse.success(authService.logOut());
    }
}
