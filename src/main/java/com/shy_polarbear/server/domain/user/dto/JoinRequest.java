package com.shy_polarbear.server.domain.user.dto;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import lombok.Getter;

@Getter
public class JoinRequest {

    private String signToken;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String profileImage;

}
