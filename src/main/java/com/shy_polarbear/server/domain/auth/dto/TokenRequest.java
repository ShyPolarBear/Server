package com.shy_polarbear.server.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequest {
    private String accessToken;
}
