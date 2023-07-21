package com.shy_polarbear.server.domain.user.dto.auth;

import lombok.Getter;

@Getter
public class ReissueRequest {

    private String refreshToken;
}
