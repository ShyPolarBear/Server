package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String explanation;

    protected Quiz(String question, String explanation) {
        this.question = question;
        this.explanation = explanation;
    }

    // test
    public void setIdForMockTest(Long mockId) {
        this.id = mockId;
    }
}
