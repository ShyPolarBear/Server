package com.shy_polarbear.server.domain.user.dto.user;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.Builder;


public class UserInfoResponse {
    private String nickName;
    private String profileImage;
    private String email;
    private String phoneNumber;

    @Builder
    private UserInfoResponse(String nickName, String profileImage, String email, String phoneNumber) {
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImage())
                .build();
    }
}
