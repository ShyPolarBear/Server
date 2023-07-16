package com.shy_polarbear.server.domain.user.dto;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class LoginRequest {

    private String oAuthType;
    private String oauthAccessToken;

}
