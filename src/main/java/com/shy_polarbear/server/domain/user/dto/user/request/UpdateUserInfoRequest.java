package com.shy_polarbear.server.domain.user.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoRequest {
    @NotBlank
    private String nickName;
    @NotBlank
    private String profileImage;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
}
