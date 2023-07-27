package com.shy_polarbear.server.domain.user.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueRequest {

    private String refreshToken;
}
