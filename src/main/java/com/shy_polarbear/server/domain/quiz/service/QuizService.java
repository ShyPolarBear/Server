package com.shy_polarbear.server.domain.quiz.service;

import com.shy_polarbear.server.domain.point.service.PointService;
import com.shy_polarbear.server.domain.quiz.dto.request.MultipleChoiceQuizScoreRequest;
import com.shy_polarbear.server.domain.quiz.dto.request.OXQuizScoreRequest;
import com.shy_polarbear.server.domain.quiz.dto.response.MultipleChoiceQuizScoreResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.OXQuizScoreResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.QuizCardResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.WhetherDailyQuizSolvedResponse;
import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.domain.quiz.model.*;
import com.shy_polarbear.server.domain.quiz.repository.*;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {
    private final UserService userService;
    private final PointService pointService;

    private final QuizRepository quizRepository;
    private final UserQuizRepository userQuizRepository;
    private final OXQuizRepository oxQuizRepository;
    private final MultipleChoiceQuizRepository multipleChoiceQuizRepository;
    private final MultipleChoiceRepository multipleChoiceRepository;

    // 데일리 퀴즈 조회 : 오늘 유저가 아직 제출하지 않은 OX 퀴즈, 객관식 퀴즈. 최신순으로 정렬 -> 1개만
    public QuizCardResponse getDailyQuiz(Long currentUserId) {
        Quiz quiz = quizRepository.findRecentQuizNotYetSolvedByUser(currentUserId)
                .orElseThrow(() -> new QuizException(ExceptionStatus.NO_MORE_DAILY_QUIZ));

        return QuizCardResponse.from(quiz);
    }

    // 복습 퀴즈 조회 : 랜덤으로 5개
    public PageResponse<QuizCardResponse> getRandomReviewQuizzes(Long currentUserId, int limit) {
        Slice<QuizCardResponse> result = quizRepository
                .findRandomQuizzesAlreadySolvedByUser(currentUserId, limit)
                .map(QuizCardResponse::from);

        Long count = quizRepository.countAllQuizzesAlreadySolvedByUser(currentUserId);
        return PageResponse.of(result, count);
    }

    // 데일리 퀴즈 풀이 여부 조회 : 오늘 0시 0분 0초를 기준으로 해당 유저가 제출한 문제를 조회
    public WhetherDailyQuizSolvedResponse getWhetherDailyQuizSolved(Long currentUserId) {
        LocalDate today = LocalDate.now();
        Optional<UserQuiz> optionalUserQuiz = userQuizRepository.findFirstSubmittedDailyQuizByUser(today.toString(), currentUserId);

        // TODO refactor: UserQuiz 책임 이동
        boolean isSubmitted = optionalUserQuiz.isPresent();    // 레코드 존재 여부
        Long quizId = isSubmitted ? optionalUserQuiz.get().getQuiz().getId() : null; // 존재 여부에 따라 id값 할당
        boolean isSolved = isSubmitted && optionalUserQuiz.get().isCorrect();

        return WhetherDailyQuizSolvedResponse.of(quizId, isSolved);
    }

    // OX 퀴즈 제출 채점
    @Transactional
    public OXQuizScoreResponse scoreOXQuizSubmission(Long currentUserId, long quizId, OXQuizScoreRequest request) {
        User user = userService.getUser(currentUserId);
        OXQuiz oxQuiz = oxQuizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_QUIZ));

        if (request.getIsTimeout()) {   // 시간초과: 오답 처리
            int pointValue = pointService.calculateQuizSubmissionTimeout();
            return OXQuizScoreResponse.ofTimeout(oxQuiz, pointValue);
        } else if (Objects.isNull(request.getAnswer())) { // 시간초과되지 않았는데 선택지가 null: 클라이언트 에러
            throw new QuizException(ExceptionStatus.QUIZ_SUBMISSION_NULL_CLIENT_ERROR);
        } else {
            // TODO refactor: OXChoice 책임 이동
            OXChoice submittedChoice = OXChoice.toEnum(request.getAnswer());
            boolean isCorrect = submittedChoice.equals(oxQuiz.getAnswer()); // 제출된 답안과 실제 답 비교

            userQuizRepository.save(UserQuiz.createUserOXQuiz(user, oxQuiz, isCorrect, submittedChoice));

            int pointValue = pointService.calculateQuizSubmissionPoint(isCorrect, user);    // 포인트 처리
            return OXQuizScoreResponse.of(oxQuiz, isCorrect, pointValue);
        }
    }

    // 객관식 퀴즈 제출 채점
    @Transactional
    public MultipleChoiceQuizScoreResponse scoreMultipleQuizSubmission(Long currentUserId, long quizId, MultipleChoiceQuizScoreRequest request) {
        User user = userService.getUser(currentUserId);
        MultipleChoiceQuiz multipleChoiceQuiz = multipleChoiceQuizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_QUIZ));
        List<MultipleChoice> multipleChoiceList = multipleChoiceRepository.findAllByMultipleChoiceQuizId(quizId);

        MultipleChoice answer = multipleChoiceList.stream().filter(MultipleChoice::getIsAnswer).findFirst()
                .orElseThrow(() -> new QuizException(ExceptionStatus.SERVER_ERROR));    // 답이 없는 퀴즈는 서버쪽 오류
        int sequence = multipleChoiceList.indexOf(answer) + 1;// 정답 선택지의 순서

        if (request.getIsTimeout()) {   // 시간초과: 오답 처리
            int pointValue = pointService.calculateQuizSubmissionTimeout();
            return MultipleChoiceQuizScoreResponse.ofTimeout(multipleChoiceQuiz, sequence, answer, pointValue);
        } else if (Objects.isNull(request.getAnswerId())) { // 시간초과되지 않았는데 선택지가 null: 클라이언트 에러
            throw new QuizException(ExceptionStatus.QUIZ_SUBMISSION_NULL_CLIENT_ERROR);
        } else {
            // TODO refactor: MultipleChoiceQuiz 책임 이동
            MultipleChoice submittedChoice = multipleChoiceList.stream().filter(it -> it.getId().equals(request.getAnswerId())).findFirst()
                    .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_CHOICE));

            boolean isCorrect = submittedChoice.equals(answer); // 제출된 답안과 실제 답 비교
            userQuizRepository.save(UserQuiz.createUserMultipleChoiceQuiz(user, multipleChoiceQuiz, isCorrect, submittedChoice));

            int pointValue = pointService.calculateQuizSubmissionPoint(isCorrect, user);    // 포인트 처리
            return MultipleChoiceQuizScoreResponse.of(multipleChoiceQuiz, sequence, answer, isCorrect, pointValue);
        }
    }

}
