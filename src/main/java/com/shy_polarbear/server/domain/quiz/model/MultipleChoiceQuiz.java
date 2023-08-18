package com.shy_polarbear.server.domain.quiz.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DiscriminatorValue("M")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipleChoiceQuiz extends Quiz {
    @OneToMany(mappedBy = "multipleChoiceQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultipleChoice> multipleChoiceList = new ArrayList<>();

    @Builder
    public MultipleChoiceQuiz(String question, String explanation, List<MultipleChoice> multipleChoiceList) {
        super(question, explanation);
        this.multipleChoiceList = multipleChoiceList;
    }
}
