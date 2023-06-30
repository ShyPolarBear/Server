package com.shy_polarbear.server.domain.quiz.entity;

import com.shy_polarbear.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQuiz {

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

    public void assignQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
