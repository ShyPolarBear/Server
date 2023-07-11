package com.shy_polarbear.server.domain.user.dto;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String oAuthType;
    private String accessToken;

    public static LoginRequest from (User user) {
        return new LoginRequest(user.getOAuthType(), user.getAccessToken());
    }
}
