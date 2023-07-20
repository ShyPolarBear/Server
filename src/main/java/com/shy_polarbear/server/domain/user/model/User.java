package com.shy_polarbear.server.domain.user.model;


import com.shy_polarbear.server.domain.quiz.model.UserQuiz;
import com.shy_polarbear.server.domain.point.model.Point;
import com.shy_polarbear.server.domain.ranking.model.Ranking;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String nickName;
    private String email;
    private String profileImage;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    //TODO: 기본값 설정하기
    private Boolean isBlackListUser;
    @OneToMany(mappedBy = "user")
    private List<UserQuiz> userQuiz = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Point> points = new ArrayList<>();

    // this 유저가 차단한 유저 리스트
    @OneToMany(mappedBy = "blockedUser")
    List<BlockedUser> blockedUsers = new ArrayList<>();
    private LocalDateTime lastLoginDate;
    private String providerId;
    private String provider;
    private String password;

    public void addUserQuiz(UserQuiz userQuiz) {
        this.userQuiz.add(userQuiz);
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    @Builder
    public User(Long id, String nickName, String email, String profileImage,
                String phoneNumber, UserRole role, Boolean isBlackListUser,
                String providerId, String provider, String password) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isBlackListUser = isBlackListUser;
        this.providerId = providerId;
        this.provider = provider;
        this.password = password;
        this.userStatus = UserStatus.ENGAGED;
    }


    public static User createUser(String nickName, String email, String profileImage,
                                  String phoneNumber, UserRole role, String providerId, String provider, PasswordEncoder passwordEncoder) {
        User user = User.builder()
                .nickName(nickName)
                .email(email)
                .profileImage(profileImage)
                .phoneNumber(phoneNumber)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .password(passwordEncoder.encode(providerId + "@password"))
                .build();
        Ranking.createRanking(user);
        return user;
    }

    //파라미터로 받은 유저를 내가 차단한다.
    public void blockUser(User userToBeBlocked) {
        BlockedUser blockedUser = BlockedUser.createBlockedUser(this, userToBeBlocked);
        blockedUsers.add(blockedUser);
    }

    public void unblockUser(User userToBeUnblocked) {
        //TODO : 차단 해제 로직 (수정 필요)
        blockedUsers.removeIf(blockedUser -> blockedUser.getBlockedUser().equals(userToBeUnblocked));
    }

}
