package com.shy_polarbear.server.domain.user.dto;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String oAuthType;
    private String oauthAccessToken;

}
