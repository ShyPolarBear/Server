package com.shy_polarbear.server.domain.quiz.model;

import lombok.AccessLevel;
import lombok.Builder;
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
    private final int time = 17;
    private String answer;
    private String explanation;

    @Builder
    private Quiz(QuizType quizType, String question, String answer, String explanation) {
        this.quizType = quizType;
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
    }

    public static Quiz createQuiz(QuizType quizType, String question, String answer, String explanation) {
        return Quiz.builder()
                .quizType(quizType)
                .question(question)
                .answer(answer)
                .explanation(explanation)
                .build();
    }
}
