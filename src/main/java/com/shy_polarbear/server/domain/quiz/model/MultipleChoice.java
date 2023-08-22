package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipleChoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multiple_chioce_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private MultipleChoiceQuiz multipleChoiceQuiz;

    @Builder
    public MultipleChoice(String content, boolean isAnswer, MultipleChoiceQuiz multipleChoiceQuiz) {
        this.content = content;
        this.isAnswer = isAnswer;
        this.multipleChoiceQuiz = multipleChoiceQuiz;
    }

}
