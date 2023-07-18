package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.domain.config.jwt.JwtDto;
import com.shy_polarbear.server.domain.config.jwt.JwtProvider;
import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.dto.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.SocialLoginRequest;
import com.shy_polarbear.server.domain.user.service.AuthService;
import com.shy_polarbear.server.domain.user.service.KakaoProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final KakaoProvider kakaoProvider;
    private final JwtProvider jwtProvider;

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody SocialLoginRequest loginRequest) {
        String accessToken = loginRequest.getSocialAccessToken();
        System.out.println(jwtProvider.isValidateToken(accessToken));
        KakaoProvider.KakaoUserInfo userInfoByAccessToken = kakaoProvider.getUserInfoByAccessToken(accessToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userInfoByAccessToken.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> loginOAuth(@RequestBody SocialLoginRequest socialLoginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.authLogin(socialLoginRequest));
    }

    @PostMapping("/join")
    public ResponseEntity<JwtDto> join(@RequestBody JoinRequest joinRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.join(joinRequest));
    }

    //access token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.reissue(reissueRequest.getRefreshToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.logOut(tokenRequest));
    }

}
