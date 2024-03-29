package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQuiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_quiz_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private boolean correct;

    @Enumerated(EnumType.STRING)
    private OXChoice submittedOXAnswer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_multiple_chioce_id")
    private MultipleChoice submittedMultipleChoiceAnswer;

    @Builder
    public UserQuiz(User user, Quiz quiz, boolean correct, OXChoice submittedOXAnswer, MultipleChoice submittedMultipleChoiceAnswer) {
        this.user = user;
        this.quiz = quiz;
        this.correct = correct;
        this.submittedOXAnswer = submittedOXAnswer;
        this.submittedMultipleChoiceAnswer = submittedMultipleChoiceAnswer;
    }

    public static UserQuiz createUserOXQuiz(User user, Quiz quiz, boolean correct, OXChoice submittedOXAnswer) {
        UserQuiz userQuiz = UserQuiz.builder()
                .user(user)
                .quiz(quiz)
                .correct(correct)
                .submittedOXAnswer(submittedOXAnswer)
                .build();

        user.addUserQuiz(userQuiz);
        return userQuiz;
    }

    public static UserQuiz createUserMultipleChoiceQuiz(User user, Quiz quiz, boolean correct, MultipleChoice submittedMultipleChoiceAnswer) {
        UserQuiz userQuiz = UserQuiz.builder()
                .user(user)
                .quiz(quiz)
                .correct(correct)
                .submittedMultipleChoiceAnswer(submittedMultipleChoiceAnswer)
                .build();

        user.addUserQuiz(userQuiz);
        return userQuiz;
    }
}
