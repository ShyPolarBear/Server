package com.shy_polarbear.server.domain.quiz.service;

import com.shy_polarbear.server.domain.quiz.dto.QuizCardResponse;
import com.shy_polarbear.server.domain.quiz.exception.QuizException;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import com.shy_polarbear.server.domain.quiz.model.Quiz;
import com.shy_polarbear.server.domain.quiz.repository.MultipleChoiceQuizRepository;
import com.shy_polarbear.server.domain.quiz.repository.OXQuizRepository;
import com.shy_polarbear.server.domain.quiz.repository.QuizRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                .orElseThrow(() -> new QuizException(ExceptionStatus.NOT_FOUND_QUIZ));

        if (quiz.getClass().equals(OXQuiz.class)) { // 퀴즈 유형에 따른 분기처리
            return QuizCardResponse.of((OXQuiz) quiz);
        } else {
            return QuizCardResponse.of((MultipleChoiceQuiz) quiz);
        }
    }

    // 데일리

    // 복습 퀴즈 조회

    // 퀴즈 채점

}
