package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.global.config.jwt.JwtDto;
import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.dto.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.SocialLoginRequest;
import com.shy_polarbear.server.domain.user.service.AuthService;
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


    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.reissue(reissueRequest.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.logOut());
    }

}
