package com.shy_polarbear.server.domain.user.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequest {
    @NotBlank
    private String socialType;
    @NotBlank
    private String socialAccessToken;

    public static SocialLoginRequest from(String socialType, String socialAccessToken) {
        return new SocialLoginRequest(socialType, socialAccessToken);
    }

}
