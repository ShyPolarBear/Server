package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<TokenResponse> join(JoinRequest joinRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.join(joinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginOAuth(LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.authLogin(loginRequest));
    }

    //access token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(ReissueRequest reissueRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.getAccessTokenByRefreshToken(reissueRequest.getRefreshToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(TokenRequest tokenRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.logOut(tokenRequest));
    }

}
