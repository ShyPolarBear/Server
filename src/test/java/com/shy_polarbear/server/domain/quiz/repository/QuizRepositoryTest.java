package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.config.TestJpaConfig;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoice;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.OXChoice;
import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(TestJpaConfig.class)
public class QuizRepositoryTest {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private OXQuizRepository oxQuizRepository;

    @Autowired
    private MultipleChoiceQuizRepository multipleChoiceQuizRepository;

    @Test
    @DisplayName("OXQuiz INSERT 성공")
    public void OXQuizInsertSuccess() {
        // given
        OXQuiz oxQuiz = OXQuiz.builder().question("질문").explanation("설명").answer(OXChoice.O).build();

        // when & then
        assertThatNoException().isThrownBy(() -> oxQuizRepository.save(oxQuiz));
    }

    @Test
    @DisplayName("MultipleChoiceQuiz INSERT 성공")
    public void MultipleChoiceQuizInsertSuccess() {
        // given
        MultipleChoiceQuiz quiz = MultipleChoiceQuiz.builder().question("질문").explanation("설명").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz).isAnswer(false).content("첫번째").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz).isAnswer(false).content("두번째").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz).isAnswer(true).content("세번째").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz).isAnswer(false).content("네번째").build();

        // when & then
        assertThatNoException().isThrownBy(() -> quizRepository.save(quiz));
    }

    @Test
    @DisplayName("Quiz INSERT 실패: question 필드 NOT NULL 제약 조건 위배")
    public void OXQuizInsertFailByQuestionFieldConstraintViolation() {
        // given
        OXQuiz invalidOXQuiz = OXQuiz.builder().question(null).explanation("설명").answer(OXChoice.O).build();
        MultipleChoiceQuiz invalidMultipleChoiceQuiz = MultipleChoiceQuiz.builder().question(null).explanation("설명").build();

        // when & then
        assertThatThrownBy(() -> oxQuizRepository.save(invalidOXQuiz))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("NULL not allowed for column \"QUESTION\"");
        assertThatThrownBy(() -> multipleChoiceQuizRepository.save(invalidMultipleChoiceQuiz))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("NULL not allowed for column \"QUESTION\"");
    }

    @Test
    @DisplayName("Quiz INSERT 실패: explanation 필드 NOT NULL 제약 조건 위배")
    public void OXQuizInsertFailByExplanationFieldConstraintViolation() {
        // given
        OXQuiz invalidOXQuiz = OXQuiz.builder().question("질문").explanation(null).answer(OXChoice.O).build();
        MultipleChoiceQuiz invalidMultipleChoiceQuiz = MultipleChoiceQuiz.builder().question("질문").explanation(null).build();

        // when & then
        assertThatThrownBy(() -> oxQuizRepository.save(invalidOXQuiz))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("NULL not allowed for column \"EXPLANATION\"");
        assertThatThrownBy(() -> multipleChoiceQuizRepository.save(invalidMultipleChoiceQuiz))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("NULL not allowed for column \"EXPLANATION\"");
    }

    @Test
    @DisplayName("MultipleChoice INSERT 실패: MultipleChoice.content NOT NULL 제약 조건 위배")
    public void MultipleChoiceInsertFailByContentConstraintViolation() {
        // given
        MultipleChoiceQuiz quiz = MultipleChoiceQuiz.builder().question("질문").explanation("설명").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz).content(null).isAnswer(false).build();    // fail trigger

        // when & then
        assertThatThrownBy(() -> multipleChoiceQuizRepository.save(quiz))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("NULL not allowed for column \"CONTENT\"");
    }

    @Test
    @DisplayName("MultipleChoice INSERT 실패: MultipleChoice.isAnswer NOT NULL 제약 조건 위배")
    public void MultipleChoiceInsertFailByIsAnswerConstraintViolation() {
        // given
        MultipleChoiceQuiz quiz = MultipleChoiceQuiz.builder().question("질문").explanation("설명").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz).content("첫번째").isAnswer(null).build();    // fail trigger

        // when & then
        assertThatThrownBy(() -> multipleChoiceQuizRepository.save(quiz))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasStackTraceContaining("NULL not allowed for column \"IS_ANSWER\"")
        ;
    }

}
