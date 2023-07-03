package com.shy_polarbear.server.domain.user.entity;


import com.shy_polarbear.server.domain.quiz.entity.UserQuiz;
import com.shy_polarbear.server.domain.point.entity.Point;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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
    private boolean isBlackListUser;
    @OneToMany(mappedBy = "user")
    private List<UserQuiz> userQuiz = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Point> points = new ArrayList<>();

    //TODO: 유저가 차단한 유저 리스트

    private String accessToken;
    private String refreshToken;

    public void addUserQuiz(UserQuiz userQuiz) {
        this.userQuiz.add(userQuiz);
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    @Builder
    private User(String nickName, String email, String profileImage, String phoneNumber, UserRole userRole) {
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
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
}
