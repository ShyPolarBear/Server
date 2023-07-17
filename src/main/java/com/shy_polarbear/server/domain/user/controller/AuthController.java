package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.domain.config.jwt.JwtDto;
import com.shy_polarbear.server.domain.config.security.PrincipalDetails;
import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.dto.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.LoginRequest;
import com.shy_polarbear.server.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> loginOAuth(LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.authLogin(loginRequest));
    }

    @PostMapping("/join")
    public ResponseEntity<JwtDto> join(JoinRequest joinRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.join(joinRequest));
    }

    //access token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> reissueToken(ReissueRequest reissueRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.reissue(reissueRequest.getRefreshToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(TokenRequest tokenRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.logOut(tokenRequest));
    }

}
