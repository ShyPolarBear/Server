package com.shy_polarbear.server.domain.user.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoRequest {

    private String nickName;
    private String profileImage;
    private String email;
    private String phoneNumber;
}
