package com.shy_polarbear.server.domain.auth.dto;

import lombok.Getter;

@Getter
public class ReissueRequest {

    private String refreshToken;
}
