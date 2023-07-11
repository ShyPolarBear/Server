package com.shy_polarbear.server.domain.user.model;

import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DormantUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormant_user_id")
    private Long id;
    private Long userId;
    private String nickName;
    private String email;
    private String profileImage;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private UserStatus userStatus;
    private boolean isBlackListUser;

    @Builder
    private DormantUser(Long userId, String nickName, String email, String profileImage, String phoneNumber, UserRole userRole, boolean isBlackListUser) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.isBlackListUser = isBlackListUser;
        this.userStatus = UserStatus.DORMANT;
    }

    public static DormantUser createDormantUser(Long userId, String nickName, String email,
                                                String profileImage, String phoneNumber,
                                                UserRole userRole, boolean isBlackListUser) {
        return DormantUser.builder()
                .userId(userId)
                .nickName(nickName)
                .email(email)
                .profileImage(profileImage)
                .phoneNumber(phoneNumber)
                .userRole(userRole)
                .isBlackListUser(isBlackListUser)
                .build();
    }
}
