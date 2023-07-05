package com.shy_polarbear.server.domain.user.entity;


import com.shy_polarbear.server.domain.quiz.entity.UserQuiz;
import com.shy_polarbear.server.domain.point.entity.Point;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(columnDefinition = "boolean default false")
    private Boolean isBlackListUser = false;
    @OneToMany(mappedBy = "user")
    private List<UserQuiz> userQuiz = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Point> points = new ArrayList<>();

    // this 유저가 차단한 유저 리스트
    @OneToMany(mappedBy = "blockedUser")
    List<BlockedUser> blockedUsers = new ArrayList<>();

    private String accessToken;
    private String refreshToken;
    private LocalDateTime lastLoginDate;

    public void addUserQuiz(UserQuiz userQuiz) {
        this.userQuiz.add(userQuiz);
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    @Builder
    private User(String nickName, String email, String profileImage, String phoneNumber, UserRole userRole, boolean isBlackListUser) {
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.userStatus = UserStatus.ACTIVE;
        this.isBlackListUser = isBlackListUser;
    }

    public static User createUser(String nickName, String email, String profileImage, String phoneNumber, UserRole userRole) {
        User user = User.builder()
                .nickName(nickName)
                .email(email)
                .profileImage(profileImage)
                .phoneNumber(phoneNumber)
                .userRole(userRole)
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
