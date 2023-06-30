package com.shy_polarbear.server.domain.user.entity;

import com.shy_polarbear.server.domain.quiz.entity.Quiz;
import com.shy_polarbear.server.domain.quiz.entity.UserQuiz;
import lombok.AccessLevel;
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
    private List<UserQuiz> userQuizs = new ArrayList<>();
    private String accessToken;
    private String refreshToken;
}
