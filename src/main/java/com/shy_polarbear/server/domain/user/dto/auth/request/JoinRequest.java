package com.shy_polarbear.server.domain.user.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank
    private String socialAccessToken;
    @NotBlank
    private String nickName;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String email;
    @NotBlank
    private String profileImage;
}
