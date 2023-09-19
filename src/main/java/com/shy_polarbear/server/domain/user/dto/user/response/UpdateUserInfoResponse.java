package com.shy_polarbear.server.domain.user.dto.user.response;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserInfoResponse {

    private Long id;
    private String nickName;
    private String profileImage;
    private String email;
    private String phoneNumber;

    @Builder
    private UpdateUserInfoResponse(Long id, String nickName, String profileImage, String email, String phoneNumber) {
        this.id = id;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static UpdateUserInfoResponse from(User user) {
        return UpdateUserInfoResponse.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImage(user.getProfileImage() == null ? "" : user.getProfileImage())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
