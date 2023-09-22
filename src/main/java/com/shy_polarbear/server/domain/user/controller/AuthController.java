package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.domain.user.dto.auth.response.LogoutResponse;
import com.shy_polarbear.server.domain.user.dto.auth.request.ReissueRequest;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.auth.jwt.JwtDto;
import com.shy_polarbear.server.domain.user.dto.auth.request.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.auth.request.SocialLoginRequest;
import com.shy_polarbear.server.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtDto> loginOAuth(@Valid @RequestBody SocialLoginRequest socialLoginRequest) {
        return ApiResponse.success(authService.authLogin(socialLoginRequest));
    }

    @PostMapping("/join")
    public ApiResponse<JwtDto> join(@Valid @RequestBody JoinRequest joinRequest) {
        return ApiResponse.success(authService.join(joinRequest));
    }

    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissueToken(@Valid @RequestBody ReissueRequest reissueRequest) {
        return ApiResponse.success(authService.reissue(reissueRequest.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(authService.logOut(principalDetails.getUser().getId()));
    }
}
