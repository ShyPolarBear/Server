package com.shy_polarbear.server.domain.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {

    private String accessToken;
    private String refreshToken;

    public static JwtDto from(String accessToken, String refreshToken) {
        return new JwtDto(accessToken, refreshToken);
    }
}
