package com.shy_polarbear.server.domain.auth.controller;

import com.shy_polarbear.server.domain.auth.dto.*;
import com.shy_polarbear.server.domain.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/join")
    public ResponseEntity<TokenResponse> join(JoinRequest joinRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(oAuthService.join(joinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginOAuth(LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(oAuthService.oAuthLogin(loginRequest));
    }

    //access token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(ReissueRequest reissueRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(oAuthService.accessTokenByRefreshToken(reissueRequest.getRefreshToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        oAuthService.logOut();
        return ResponseEntity.status(HttpStatus.OK)
                .body(oAuthService.logOut());
    }

}
