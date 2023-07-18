package com.shy_polarbear.server.domain.user.dto;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    private String socialAccessToken;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String profileImage;

}
