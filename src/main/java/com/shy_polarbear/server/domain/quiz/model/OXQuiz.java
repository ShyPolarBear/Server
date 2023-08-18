package com.shy_polarbear.server.domain.quiz.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

// TODO: Getter, constructor, builder은 어디서 만드는게 좋지? 자식쪽에서 하는게 맞을듯. Quiz에서 만들고 다 적용되면 좋긴한데.
@Getter
@Entity
@DiscriminatorValue("OX")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OXQuiz extends Quiz {
    @Enumerated(EnumType.STRING)
    private OXChoice answer;

    @Builder
    public OXQuiz(String question, String explanation, OXChoice answer) {
        super(question, explanation);
        this.answer = answer;
    }
}
