package com.shy_polarbear.server.domain.quiz.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private QuizType quizType;
    private String question;
    private int time;
    private String answer;
    private String explanation;
}
