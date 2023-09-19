package com.shy_polarbear.server.domain.quiz.service;

import com.shy_polarbear.server.domain.point.model.PointType;
import com.shy_polarbear.server.domain.point.service.PointService;
import com.shy_polarbear.server.domain.quiz.dto.QuizType;
import com.shy_polarbear.server.domain.quiz.dto.request.MultipleChoiceQuizScoreRequest;
import com.shy_polarbear.server.domain.quiz.dto.request.OXQuizScoreRequest;
import com.shy_polarbear.server.domain.quiz.dto.response.MultipleChoiceQuizScoreResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.OXQuizScoreResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.QuizCardResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.WhetherDailyQuizSolvedResponse;
import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.domain.quiz.model.*;
import com.shy_polarbear.server.domain.quiz.repository.*;
import com.shy_polarbear.server.domain.quiz.template.QuizTemplate;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.domain.user.template.UserTemplate;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {
    @InjectMocks
    QuizService quizService;

    @Mock
    UserService userService;
    @Mock
    PointService pointService;

    @Mock
    QuizRepository quizRepository;
    @Mock
    UserQuizRepository userQuizRepository;
    @Mock
    OXQuizRepository oxQuizRepository;
    @Mock
    MultipleChoiceQuizRepository multipleChoiceQuizRepository;
    @Mock
    MultipleChoiceRepository multipleChoiceRepository;

    private static final User mockUser = UserTemplate.createDummyUser();
    private static final OXQuiz mockOXQuiz = QuizTemplate.createDummyOXQuizA();
    private static final List<Quiz> mockOXQuizList = QuizTemplate.createDummyOXQuizList();
    private static final MultipleChoiceQuiz mockMultipleChoiceQuiz = QuizTemplate.createDummyMultipleChoiceQuizA();
    private static final List<MultipleChoice> mockMultipleChoiceList = QuizTemplate.createDummyMultipleChoiceList();
    private static final MultipleChoice correctChoice = mockMultipleChoiceList.stream().filter(MultipleChoice::getIsAnswer).findFirst().orElseThrow();
    private static final MultipleChoice wrongChoice = mockMultipleChoiceList.stream().filter(it -> it.getIsAnswer().equals(false)).findFirst().orElseThrow();


    @Test
    @DisplayName("getDailyQuiz 성공: 유저가 제출하지 않은 가장 최근 퀴즈를 조회")
    public void getDailyQuizSuccess() {
        // given
        given(quizRepository.findRecentQuizNotYetSolvedByUser(UserTemplate.ID)).willReturn(Optional.of(mockOXQuiz));

        // when
        QuizCardResponse response = quizService.getDailyQuiz(UserTemplate.ID);

        // then
        assertThat(response.quizId()).isEqualTo(mockOXQuiz.getId());
        assertThat(response.type()).isEqualTo(QuizType.OX.getValue());
        assertThat(response.time()).isEqualTo(BusinessLogicConstants.OX_QUIZ_TIME_LIMIT);
        assertThat(response.question()).isEqualTo(mockOXQuiz.getQuestion());
        assertThat(response.choices()).isNull();
    }

    @Test
    @DisplayName("getDailyQuiz 실패 : 퀴즈 존재하지 않음 -> NOT FOUND 에러")
    public void getDailyQuizNoMoreDailyQuizFail() {
        // given
        given(quizRepository.findRecentQuizNotYetSolvedByUser(UserTemplate.ID)).willReturn(Optional.empty());  // 레코드 0개

        // when & then
        assertThatThrownBy(() -> quizService.getDailyQuiz(UserTemplate.ID))
                .isInstanceOf(QuizException.class)
                .hasMessage(ExceptionStatus.NO_MORE_DAILY_QUIZ.getMessage());
    }

    @Test
    @DisplayName("getRandomReviewQuizzes 성공 : 랜덤 조회")
    public void getRandomReviewQuizzesSuccess() {
        // given
        final int limit = 5;
        given(quizRepository.findRandomQuizzesAlreadySolvedByUser(UserTemplate.ID, limit)).willReturn(new SliceImpl<>(mockOXQuizList));
        given(quizRepository.countAllQuizzesAlreadySolvedByUser(UserTemplate.ID)).willReturn(Long.valueOf(limit));

        // when
        PageResponse<QuizCardResponse> response = quizService.getRandomReviewQuizzes(UserTemplate.ID, limit);

        // then
        List<QuizCardResponse> mockResponse = mockOXQuizList.stream().map(it -> QuizCardResponse.of((OXQuiz) it)).toList();
        assertThat(response.getContent()).isEqualTo(mockResponse);
    }

    @Test
    @DisplayName("getWhetherDailyQuizSolved 성공: 존재 && 맞았음 -> isSolved는 true여야 한다")
    public void getWhetherDailyQuizSolvedSuccess() {
        // given
        boolean isCorrect = true;
        final UserQuiz mockUserQuiz = UserQuiz.builder()
                .user(mockUser)
                .quiz(mockOXQuiz)
                .submittedOXAnswer(OXChoice.O)
                .correct(isCorrect)
                .build();

        given(userQuizRepository.findFirstSubmittedDailyQuizByUser(any(), anyLong())).willReturn(Optional.ofNullable(mockUserQuiz));
        // when
        WhetherDailyQuizSolvedResponse response = quizService.getWhetherDailyQuizSolved(UserTemplate.ID);

        // then
        assertThat(response.isSolved()).isTrue();   // TRUE
        assertThat(response.quizId()).isEqualTo(mockUserQuiz.getQuiz().getId());
    }

    @Test
    @DisplayName("getWhetherDailyQuizSolved 성공: 존재 && 틀렸음 -> isSolved는 false여야 한다")
    public void getWhetherDailyQuizSolvedWrongFail() {
        // given
        boolean isCorrect = false;
        final UserQuiz mockUserQuiz = UserQuiz.builder()
                .user(mockUser)
                .quiz(mockOXQuiz)
                .submittedOXAnswer(OXChoice.O)
                .correct(isCorrect)
                .build();

        given(userQuizRepository.findFirstSubmittedDailyQuizByUser(any(), anyLong())).willReturn(Optional.ofNullable(mockUserQuiz));
        // when
        WhetherDailyQuizSolvedResponse response = quizService.getWhetherDailyQuizSolved(UserTemplate.ID);

        // then
        assertThat(response.isSolved()).isFalse();  // FALSE
        assertThat(response.quizId()).isEqualTo(mockUserQuiz.getQuiz().getId());
    }

    @Test
    @DisplayName("getWhetherDailyQuizSolved 성공: 존재하지 않음 -> isSolved는 false여야 한다")
    public void getWhetherDailyQuizSolvedNoRecordFail() {
        // given
        given(userQuizRepository.findFirstSubmittedDailyQuizByUser(any(), anyLong())).willReturn(Optional.empty());
        // when
        WhetherDailyQuizSolvedResponse response = quizService.getWhetherDailyQuizSolved(UserTemplate.ID);

        // then
        assertThat(response.isSolved()).isFalse();  // FALSE
        assertThat(response.quizId()).isNull();
    }

    @Test
    @DisplayName("scoreOXQuizSubmission 성공: 시간 제한 준수 && 맞았음 -> isCorrect는 true이고 point는 100이어야 한다")
    public void scoreOXQuizSubmissionCorrectInTimeSuccess() {
        // given
        boolean mockIsCorrect = true, isTimeout = false;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(oxQuizRepository.findById(anyLong())).willReturn(Optional.of(mockOXQuiz));
        given(pointService.calculateQuizSubmissionPoint(mockIsCorrect, mockUser)).willReturn(PointType.SOLVE_QUIZ.getValue());

        // when
        OXQuizScoreRequest mockRequest = new OXQuizScoreRequest(OXChoice.O.getValue(), isTimeout);
        OXQuizScoreResponse response = quizService.scoreOXQuizSubmission(UserTemplate.ID, mockOXQuiz.getId(), mockRequest);

        // then
        assertThat(response.quizId()).isEqualTo(mockOXQuiz.getId());
        assertThat(response.point()).isEqualTo(PointType.SOLVE_QUIZ.getValue());
        assertThat(response.isCorrect()).isTrue();
    }

    @Test
    @DisplayName("scoreOXQuizSubmission 성공: 시간 제한 준수 && 틀렸음 -> isCorrect는 false이고 point는 0이어야 한다")
    public void scoreOXQuizSubmissionWrongInTimeSuccess() {
        // given
        boolean mockIsCorrect = false, isTimeout = false;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(oxQuizRepository.findById(anyLong())).willReturn(Optional.of(mockOXQuiz));
        given(pointService.calculateQuizSubmissionPoint(mockIsCorrect, mockUser)).willReturn(PointType.NOT_SOLVE_QUIZ.getValue());

        // when
        OXQuizScoreRequest mockRequest = new OXQuizScoreRequest(OXChoice.X.getValue(), isTimeout);
        OXQuizScoreResponse response = quizService.scoreOXQuizSubmission(UserTemplate.ID, mockOXQuiz.getId(), mockRequest);

        // then
        assertThat(response.quizId()).isEqualTo(mockOXQuiz.getId());
        assertThat(response.point()).isEqualTo(PointType.NOT_SOLVE_QUIZ.getValue());
        assertThat(response.isCorrect()).isFalse();
    }

    @Test
    @DisplayName("scoreOXQuizSubmission 성공: 시간 제한 위반 -> isCorrect는 false이고 point는 0이어야 한다")
    public void scoreOXQuizSubmissionTimeoutSuccess() {
        // given
        boolean isTimeout = true;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(oxQuizRepository.findById(anyLong())).willReturn(Optional.of(mockOXQuiz));

        // when
        OXQuizScoreRequest mockRequest = new OXQuizScoreRequest(null, isTimeout);
        OXQuizScoreResponse response = quizService.scoreOXQuizSubmission(UserTemplate.ID, mockOXQuiz.getId(), mockRequest);

        // then
        assertThat(response.quizId()).isEqualTo(mockOXQuiz.getId());
        assertThat(response.point()).isEqualTo(PointType.NOT_SOLVE_QUIZ.getValue());
        assertThat(response.isCorrect()).isFalse();
    }

    @Test
    @DisplayName("scoreOXQuizSubmission 실패 : 시간 제한 준수 && 제출한 답이 없음 -> 클라이언트 에러")
    public void scoreOXQuizSubmissionNullInTimeFail() {
        // given
        boolean isTimeout = false;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(oxQuizRepository.findById(anyLong())).willReturn(Optional.of(mockOXQuiz));


        // when & then
        OXQuizScoreRequest mockRequest = new OXQuizScoreRequest(null, isTimeout);  // 에러 원인
        assertThatThrownBy(() -> quizService.scoreOXQuizSubmission(UserTemplate.ID, mockOXQuiz.getId(), mockRequest))
                .isInstanceOf(QuizException.class)
                .hasMessage(ExceptionStatus.QUIZ_SUBMISSION_NULL_CLIENT_ERROR.getMessage());
    }

    @Test
    @DisplayName("scoreMultipleQuizSubmission 성공 : 시간 제한 준수 && 맞았음 -> isCorrect는 true이고 point는 100이어야 한다")
    public void scoreMultipleQuizSubmissionCorrectInTimeSuccess() {
        // given
        boolean mockIsCorrect = true, isTimeout = false;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(multipleChoiceQuizRepository.findById(anyLong())).willReturn(Optional.of(mockMultipleChoiceQuiz));
        given(multipleChoiceRepository.findAllByMultipleChoiceQuizId(anyLong())).willReturn(mockMultipleChoiceList);
        given(pointService.calculateQuizSubmissionPoint(mockIsCorrect, mockUser)).willReturn(PointType.SOLVE_QUIZ.getValue());

        // when
        MultipleChoiceQuizScoreRequest mockRequest = new MultipleChoiceQuizScoreRequest(correctChoice.getId(), isTimeout);
        MultipleChoiceQuizScoreResponse response = quizService.scoreMultipleQuizSubmission(UserTemplate.ID, mockMultipleChoiceQuiz.getId(), mockRequest);

        // then
        assertThat(response.quizId()).isEqualTo(mockMultipleChoiceQuiz.getId());
        assertThat(response.point()).isEqualTo(PointType.SOLVE_QUIZ.getValue());
        assertThat(response.isCorrect()).isTrue();
    }

    @Test
    @DisplayName("scoreMultipleQuizSubmission 성공 : 시간 제한 준수 && 틀렸음 -> isCorrect는 false이고 point는 0이어야 한다")
    public void scoreMultipleQuizSubmissionWrongInTimeSuccess() {
        // given
        boolean mockIsCorrect = false, isTimeout = false;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(multipleChoiceQuizRepository.findById(anyLong())).willReturn(Optional.of(mockMultipleChoiceQuiz));
        given(multipleChoiceRepository.findAllByMultipleChoiceQuizId(anyLong())).willReturn(mockMultipleChoiceList);
        given(pointService.calculateQuizSubmissionPoint(mockIsCorrect, mockUser)).willReturn(PointType.NOT_SOLVE_QUIZ.getValue());

        // when
        MultipleChoiceQuizScoreRequest mockRequest = new MultipleChoiceQuizScoreRequest(wrongChoice.getId(), isTimeout);
        MultipleChoiceQuizScoreResponse response = quizService.scoreMultipleQuizSubmission(UserTemplate.ID, mockMultipleChoiceQuiz.getId(), mockRequest);

        // then
        assertThat(response.quizId()).isEqualTo(mockMultipleChoiceQuiz.getId());
        assertThat(response.point()).isEqualTo(PointType.NOT_SOLVE_QUIZ.getValue());
        assertThat(response.isCorrect()).isFalse();
    }

    @Test
    @DisplayName("scoreMultipleQuizSubmission 성공 : 시간 제한 위반 -> isCorrect는 false이고 point는 0이어야 한다")
    public void scoreMultipleQuizSubmissionTimeoutSuccess() {
        // given
        boolean isTimeout = true;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(multipleChoiceQuizRepository.findById(anyLong())).willReturn(Optional.of(mockMultipleChoiceQuiz));
        given(multipleChoiceRepository.findAllByMultipleChoiceQuizId(anyLong())).willReturn(mockMultipleChoiceList);

        // when
        MultipleChoiceQuizScoreRequest mockRequest = new MultipleChoiceQuizScoreRequest(null, isTimeout);
        MultipleChoiceQuizScoreResponse response = quizService.scoreMultipleQuizSubmission(UserTemplate.ID, mockMultipleChoiceQuiz.getId(), mockRequest);

        // then
        assertThat(response.quizId()).isEqualTo(mockMultipleChoiceQuiz.getId());
        assertThat(response.point()).isEqualTo(PointType.NOT_SOLVE_QUIZ.getValue());
        assertThat(response.isCorrect()).isFalse();
    }

    @Test
    @DisplayName("scoreMultipleQuizSubmission 실패 : 시간 제한 준수 && 제출한 답이 없음 -> 클라이언트 에러")
    public void scoreMultipleQuizSubmissionNullInTimeFail() {
        // given
        boolean isTimeout = false;
        given(userService.getUser(anyLong())).willReturn(mockUser);
        given(multipleChoiceQuizRepository.findById(anyLong())).willReturn(Optional.of(mockMultipleChoiceQuiz));
        given(multipleChoiceRepository.findAllByMultipleChoiceQuizId(anyLong())).willReturn(mockMultipleChoiceList);

        // when & then
        MultipleChoiceQuizScoreRequest mockRequest = new MultipleChoiceQuizScoreRequest(null, isTimeout);  // 에러 원인
        assertThatThrownBy(() -> quizService.scoreMultipleQuizSubmission(UserTemplate.ID, mockMultipleChoiceQuiz.getId(), mockRequest))
                .isInstanceOf(QuizException.class)
                .hasMessage(ExceptionStatus.QUIZ_SUBMISSION_NULL_CLIENT_ERROR.getMessage());
    }
}
