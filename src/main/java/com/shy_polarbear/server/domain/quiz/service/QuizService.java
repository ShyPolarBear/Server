package com.shy_polarbear.server.domain.quiz.service;

import com.shy_polarbear.server.domain.quiz.dto.QuizCardResponse;
import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import com.shy_polarbear.server.domain.quiz.model.Quiz;
import com.shy_polarbear.server.domain.quiz.repository.MultipleChoiceQuizRepository;
import com.shy_polarbear.server.domain.quiz.repository.OXQuizRepository;
import com.shy_polarbear.server.domain.quiz.repository.QuizRepository;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.NoCountPageResponse;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final MultipleChoiceQuizRepository multipleChoiceQuizRepository;
    private final OXQuizRepository oxQuizRepository;

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

    // 퀴즈 채점


    // 퀴즈 유형에 따른 분기처리
    private QuizCardResponse buildQuizCardResponseFromQuiz(Quiz quiz) {
        if (quiz.getClass().equals(OXQuiz.class)) {
            return QuizCardResponse.of((OXQuiz) quiz);
        } else {
            return QuizCardResponse.of((MultipleChoiceQuiz) quiz);
        }
    }
}
