package com.shy_polarbear.server.domain.quiz.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipleChoiceQuiz extends Quiz {
    @OneToMany(mappedBy = "multipleChoiceQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultipleChoice> multipleChoiceList = new ArrayList<>();

    @Builder
    public MultipleChoiceQuiz(String question, String explanation) {
        super(QuizType.MULTIPLE_CHOICE, question, explanation);
    }
}
