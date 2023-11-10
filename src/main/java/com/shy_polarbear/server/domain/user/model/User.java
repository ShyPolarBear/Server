package com.shy_polarbear.server.domain.user.model;


import com.shy_polarbear.server.domain.notification.model.Notification;
import com.shy_polarbear.server.domain.quiz.model.UserQuiz;
import com.shy_polarbear.server.domain.point.model.Point;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String nickName;
    @Column(nullable = false)
    private String email;
    private String profileImage;
    @Column(nullable = false)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isBlackListUser;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuiz> userQuiz = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Point> points = new ArrayList<>();

    // this 유저가 차단한 유저 리스트
    @OneToMany(mappedBy = "blockedUser")
    private List<BlockedUser> blockedUsers = new ArrayList<>();
    private LocalDateTime lastLoginDate;
    @Column(nullable = false, unique = true)
    private String providerId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType provider;
    @Column(nullable = false, unique = true)
    private String password;
    @Column(unique = true)
    private String fcmToken;
    @OneToMany(mappedBy = "receiver")
    private List<Notification> notificationList = new ArrayList<>();

    public void addUserQuiz(UserQuiz userQuiz) {
        this.userQuiz.add(userQuiz);
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    @Builder
    public User(Long id, String nickName, String email, String profileImage,
                String phoneNumber, UserRole role,
                String providerId, ProviderType provider, String password) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.providerId = providerId;
        this.provider = provider;
        this.password = password;
        this.userStatus = UserStatus.ENGAGED;
    }


    public static User createUser(String nickName, String email, String profileImage,
                                  String phoneNumber, UserRole role, String providerId, ProviderType provider, PasswordEncoder passwordEncoder) {
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

    public void updateInfo(String nickName, String profileImage, String email, String phoneNumber) {
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public boolean isSameNickName(String nickName) {
        return Objects.equals(this.nickName, nickName);
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void removeFcmToken() {
        this.fcmToken = null;
    }

    public void addNotification(Notification notification) {
        this.notificationList.add(notification);
    }

    // test
    public void setIdForTest(Long mockId) {
        this.id = mockId;
    }
}
