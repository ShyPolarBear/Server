package com.shy_polarbear.server.domain.quiz.repository;

import com.shy_polarbear.server.config.TestJpaConfig;
import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.domain.quiz.model.*;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.template.UserTemplate;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DataJpaTest
@Import(TestJpaConfig.class)
public class QuizRepositoryTest {
    private User dummyUser = UserTemplate.createDummyUser();
    private OXQuiz dummyMostRecentNotSubmittedOXQuiz;
    private final int DUMMY_SUBMITTED_QUIZ_SIZE = 100;
    private final int DUMMY_NOT_SUBMITTED_QUIZ_SIZE = 100;

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
        this.dummyUser = userRepository.save(UserTemplate.createDummyUser());

        for (int i = 0; i < DUMMY_SUBMITTED_QUIZ_SIZE; i++) {// 제출된 퀴즈
            OXQuiz oxQuiz = quizRepository.save(OXQuiz.builder()
                    .question("질문" + i)
                    .explanation("설명" + i)
                    .answer(OXChoice.O)
                    .build());
            userQuizRepository.save(UserQuiz.createUserOXQuiz(dummyUser, oxQuiz, false, OXChoice.X)); // 제출 처리. 정답이 아니더라도 제출한 것
        }

        for (int i = 0; i < DUMMY_NOT_SUBMITTED_QUIZ_SIZE; i++) {// 제출되지 않은 퀴즈
            OXQuiz oxQuiz = quizRepository.save(OXQuiz.builder()
                    .question("질문" + i)
                    .explanation("설명" + i)
                    .answer(OXChoice.O)
                    .build());
            if (i == DUMMY_NOT_SUBMITTED_QUIZ_SIZE - 1) dummyMostRecentNotSubmittedOXQuiz = oxQuiz; // 가장 최근 퀴즈
        }
    }

    @Test
    @DisplayName("INSERT OXQuiz 성공")
    public void OXQuizInsertSuccess() {
        // given
        OXQuiz oxQuiz = OXQuiz.builder().question("질문").explanation("설명").answer(OXChoice.O).build();

        // when & then
        assertThatNoException().isThrownBy(() -> oxQuizRepository.save(oxQuiz));
    }

    @Test
    @DisplayName("INSERT MultipleChoiceQuiz 성공")
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
    @DisplayName("INSERT Quiz 실패: question 필드 NOT NULL 제약 조건 위배")
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
    @DisplayName("INSERT Quiz 실패: explanation 필드 NOT NULL 제약 조건 위배")
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
    @DisplayName("INSERT MultipleChoice 실패: MultipleChoice.content NOT NULL 제약 조건 위배")
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
    @DisplayName("INSERT MultipleChoice 실패: MultipleChoice.isAnswer NOT NULL 제약 조건 위배")
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
    @DisplayName("SELECT findAll 성공")
    public void findAllSuccess() {
        // given

        // when & then
        assertThat(quizRepository.findAll().size())
                .isEqualTo(DUMMY_SUBMITTED_QUIZ_SIZE + DUMMY_NOT_SUBMITTED_QUIZ_SIZE);
    }

    @Test
    @DisplayName("SELECT findRecentQuizNotYetSolvedByUser 성공: 아직 제출하지 않은 퀴즈중 가장 최근의 퀴즈만 조회한다")
    public void findRecentQuizNotYetSolvedByUserSuccess() {
        // given

        // when & then
        assertThat(quizRepository.findRecentQuizNotYetSolvedByUser(dummyUser.getId())
                .orElseThrow(() -> new QuizException(ExceptionStatus.NO_MORE_DAILY_QUIZ)))
                .isEqualTo(dummyMostRecentNotSubmittedOXQuiz);
    }

    @Test
    @DisplayName("SELECT findRandomQuizzesAlreadySolvedByUser 성공: 제출한 퀴즈만 조회한다 && 결과가 랜덤해야 한다")
    public void findRandomQuizzesAlreadySolvedByUserSuccess() {
        // given
        final int limit = 10;

        // when
        Slice<Quiz> queryResult = quizRepository.findRandomQuizzesAlreadySolvedByUser(dummyUser.getId(), limit);
        List<Quiz> queryResultA = quizRepository.findRandomQuizzesAlreadySolvedByUser(dummyUser.getId(), limit).toList();
        List<Quiz> queryResultB = quizRepository.findRandomQuizzesAlreadySolvedByUser(dummyUser.getId(), limit).toList();
        List<Quiz> queryResultC = quizRepository.findRandomQuizzesAlreadySolvedByUser(dummyUser.getId(), limit).toList();

        // then
        assertThat(queryResult.hasContent()).isEqualTo(true);
        assertThat(queryResult.toList().size()).isEqualTo(limit);

        assertThat(queryResultA.containsAll(queryResultB)).isFalse();   // 결과가 매번 달라야 함
        assertThat(queryResultA.containsAll(queryResultC)).isFalse();
        assertThat(queryResultB.containsAll(queryResultC)).isFalse();
    }

    @Test
    @DisplayName("SELECT countAllRecentQuizzesAlreadySolvedByUser 성공: 제출한 퀴즈의 카운트를 조회한다")
    public void countAllRecentQuizzesAlreadySolvedByUserSuccess() {
        // given

        // when
        Long count = quizRepository.countAllQuizzesAlreadySolvedByUser(dummyUser.getId());

        // then
        assertThat(count).isEqualTo(DUMMY_SUBMITTED_QUIZ_SIZE);
    }
}
