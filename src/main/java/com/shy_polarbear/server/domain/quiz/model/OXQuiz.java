package com.shy_polarbear.server.domain.quiz.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OXQuiz extends Quiz {

    @Enumerated(EnumType.STRING)
    private OXChoice answer;

    @Builder
    public OXQuiz(String question, String explanation, OXChoice answer) {
        super(QuizType.OX, question, explanation);
        this.answer = answer;
    }

}
