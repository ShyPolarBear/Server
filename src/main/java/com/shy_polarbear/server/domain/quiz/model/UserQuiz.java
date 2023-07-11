package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQuiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_quiz_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private boolean isCorrect;
    private String userAnswer;

    //연관관계 편의 메서드
    public void assignUser(User user) {
        this.user = user;
        user.addUserQuiz(this);
    }

    @Builder
    private UserQuiz(User user, Quiz quiz, String userAnswer) {
        this.user = user;
        this.quiz = quiz;
        this.userAnswer = userAnswer;
    }

    public static UserQuiz createUserQuiz(User user, Quiz quiz, String userAnswer) {
        UserQuiz userQuiz = UserQuiz.builder()
                .user(user)
                .quiz(quiz)
                .userAnswer(userAnswer)
                .build();
        user.addUserQuiz(userQuiz);
        return userQuiz;
    }
}
