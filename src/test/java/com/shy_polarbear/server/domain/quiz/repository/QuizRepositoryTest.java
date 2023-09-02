package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.config.TestJpaConfig;
import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.domain.quiz.model.*;
import com.shy_polarbear.server.domain.user.model.ProviderType;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestJpaConfig.class)
public class QuizRepositoryTest {
    private User dummyUser;
    private OXQuiz dummyOXQuiz;
    private final int DUMMY_SUBMITTED_QUIZ_SIZE = 100;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private OXQuizRepository oxQuizRepository;

    @Autowired
    private MultipleChoiceQuizRepository multipleChoiceQuizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @BeforeEach
    void setUp() {
        this.dummyUser = userRepository.save(User.builder()
                .nickName("ws")
                .email("ws@naver.com")
                .profileImage(null)
                .phoneNumber("010558820")
                .role(UserRole.ROLE_USR)
                .provider(ProviderType.KAKAO)
                .providerId("1")
                .password("1@password")
                .build());

        this.dummyOXQuiz = quizRepository.save(OXQuiz.builder() // 제출되지 않을 퀴즈
                .question("질문 2")
                .explanation("설명 2")
                .answer(OXChoice.O)
                .build());

        for (int i = 0; i < DUMMY_SUBMITTED_QUIZ_SIZE; i++) {// 제출된 퀴즈 100개
            OXQuiz oxQuiz = quizRepository.save(OXQuiz.builder()
                    .question("질문" + i)
                    .explanation("설명" + i)
                    .answer(OXChoice.O)
                    .build());
            userQuizRepository.save(UserQuiz.createUserOXQuiz(dummyUser, oxQuiz, false, OXChoice.X)); // 제출 처리. 정답이 아니더라도 제출한 것
        }
    }

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
                .hasStackTraceContaining("NULL not allowed for column \"IS_ANSWER\"");
    }

    @Test
    @DisplayName("findRecentQuizNotYetSolvedByUser 성공: 아직 제출하지 않은 퀴즈만 조회한다")
    public void findRecentQuizNotYetSolvedByUserSuccess() {
        // given

        // when & then
        assertThat(quizRepository.findRecentQuizNotYetSolvedByUser(dummyUser.getId())
                .orElseThrow(() -> new QuizException(ExceptionStatus.NO_MORE_DAILY_QUIZ)))
                .isEqualTo(dummyOXQuiz);
    }

    @Test
    @DisplayName("findRecentQuizzesAlreadySolvedByUser 성공: 제출한 퀴즈만 조회한다")
    public void findRecentQuizzesAlreadySolvedByUserSuccess() {
        // given
        final int limit = 10;

        // when
        Slice<Quiz> queryResult = quizRepository.findRecentQuizzesAlreadySolvedByUser(dummyUser.getId(), limit);

        // then
        assertThat(queryResult.hasContent()).isEqualTo(true);
        assertThat(queryResult.toList().size()).isEqualTo(limit);
    }

    @Test
    @DisplayName("countAllRecentQuizzesAlreadySolvedByUser 성공: 제출한 퀴즈의 카운트를 조회한다")
    public void countAllRecentQuizzesAlreadySolvedByUserSuccess() {
        // given

        // when
        Long count = quizRepository.countAllRecentQuizzesAlreadySolvedByUser(dummyUser.getId());

        // then
        assertThat(count).isEqualTo(DUMMY_SUBMITTED_QUIZ_SIZE);
    }
}
