package com.shy_polarbear.server.domain.user.entity;

import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private UserRole userRole;
    private UserStatus userStatus;
    private boolean isBlackListUser;

}
