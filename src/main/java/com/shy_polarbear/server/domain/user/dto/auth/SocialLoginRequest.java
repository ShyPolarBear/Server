package com.shy_polarbear.server.domain.user.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequest {

    private String socialType;
    private String socialAccessToken;

    public static SocialLoginRequest from(String socialType, String socialAccessToken) {
        return new SocialLoginRequest(socialType, socialAccessToken);
    }

}
