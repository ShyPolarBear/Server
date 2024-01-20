package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.global.common.model.BaseEntity;
import com.shy_polarbear.server.global.common.util.profiles.CustomProfileUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false, length = 1000)
    private String explanation;

    @Column(nullable = false, name = "quiz_type")
    @Enumerated(EnumType.STRING)
    private QuizType type;

    protected Quiz(QuizType type, String question, String explanation) {
        this.type = type;
        this.question = question;
        this.explanation = explanation;
    }

    // TODO refactor: Mockito spy 대체
    // test
    public void setIdForMockTest(Long mockId) {
        CustomProfileUtils.validateIsProfileNullOrTest();

        this.id = mockId;
    }
}
