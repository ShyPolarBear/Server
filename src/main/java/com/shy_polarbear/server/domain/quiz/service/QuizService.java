package com.shy_polarbear.server.domain.quiz.service;

import com.shy_polarbear.server.domain.point.service.PointService;
import com.shy_polarbear.server.domain.quiz.dto.*;
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

import java.util.List;


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

    //     데일리 퀴즈 조회 : 현재 유저가 아직 풀지 않은 OX 퀴즈, 객관식 퀴즈. 최신순으로 정렬 -> 1개만
    public QuizCardResponse getDailyQuiz(Long currentUserId) {
        Quiz quiz = quizRepository.findRecentQuizNotYetSolvedByUser(currentUserId)
                .orElseThrow(() -> new QuizException(ExceptionStatus.NO_MORE_DAILY_QUIZ));

        return buildQuizCardResponseFromQuiz(quiz);
    }

    // 복습 퀴즈 조회 : 최신순으로 5개
    public PageResponse<QuizCardResponse> getReviewQuizzes(Long currentUserId, int limit) {
        Slice<QuizCardResponse> result = quizRepository
                .findRecentQuizzesAlreadySolvedByUser(currentUserId, limit)
                .map(this::buildQuizCardResponseFromQuiz);

        Long count = quizRepository.countAllRecentQuizzesAlreadySolvedByUser(currentUserId);
        return PageResponse.of(result, count);
    }

    // OX 퀴즈 채점
    @Transactional
    public OXQuizScoreResponse scoreOXQuizSubmission(Long currentUserId, long quizId, OXQuizScoreRequest request) {
        User user = userService.getUser(currentUserId);
        OXQuiz oxQuiz = oxQuizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_QUIZ));

        OXChoice submittedChoice = OXChoice.toEnum(request.answer());
        boolean isCorrect = submittedChoice.equals(oxQuiz.getAnswer()); // 제출된 답안과 실제 답 비교
        int pointValue = pointService.calculateQuizSubmissionPoint(isCorrect, user);    // 포인트 처리

        userQuizRepository.save(UserQuiz.builder() // UserQuiz 레코드 저장
                .user(user)
                .quiz(oxQuiz)
                .submittedOXAnswer(submittedChoice)
                .isCorrect(isCorrect)
                .build());

        return OXQuizScoreResponse.of(oxQuiz, isCorrect, pointValue);
    }

    // 객관식 퀴즈 채점
    @Transactional
    public MultipleChoiceQuizScoreResponse scoreMultipleQuizSubmission(Long currentUserId, long quizId, MultipleChoiceQuizScoreRequest request) {
        User user = userService.getUser(currentUserId);
        MultipleChoiceQuiz multipleChoiceQuiz = multipleChoiceQuizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_QUIZ));
        List<MultipleChoice> multipleChoiceList = multipleChoiceRepository.findAllByMultipleChoiceQuizId(quizId);

        Long submittedChoiceId = request.answerId();
        MultipleChoice submittedChoice = multipleChoiceList.stream().filter(it -> it.getId().equals(submittedChoiceId)).findFirst()
                .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_CHOICE));
        MultipleChoice answer = multipleChoiceList.stream().filter(MultipleChoice::isAnswer).findFirst()
                .orElseThrow(() -> new QuizException(ExceptionStatus.SERVER_ERROR));    // 답이 없는 퀴즈는 서버쪽 오류

        boolean isCorrect = submittedChoice.equals(answer); // 제출된 답안과 실제 답 비교
        int pointValue = pointService.calculateQuizSubmissionPoint(isCorrect, user);    // 포인트 처리

        userQuizRepository.save(UserQuiz.builder() // UserQuiz 레코드 저장
                .user(user)
                .quiz(multipleChoiceQuiz)
                .submittedMultipleChoiceAnswer(submittedChoice)
                .isCorrect(isCorrect)
                .build());

        return MultipleChoiceQuizScoreResponse.of(multipleChoiceQuiz, answer.getId(), isCorrect, pointValue);
    }

    // 퀴즈 유형에 따른 분기처리
    private QuizCardResponse buildQuizCardResponseFromQuiz(Quiz quiz) {
        if (quiz.getClass().equals(OXQuiz.class)) {
            return QuizCardResponse.of((OXQuiz) quiz);
        } else {
            return QuizCardResponse.of((MultipleChoiceQuiz) quiz);
        }
    }
}
