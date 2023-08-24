package com.shy_polarbear.server.domain.quiz.controller;

import com.shy_polarbear.server.domain.quiz.dto.request.MultipleChoiceQuizScoreRequest;
import com.shy_polarbear.server.domain.quiz.dto.request.OXQuizScoreRequest;
import com.shy_polarbear.server.domain.quiz.dto.response.MultipleChoiceQuizScoreResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.OXQuizScoreResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.QuizCardResponse;
import com.shy_polarbear.server.domain.quiz.dto.response.WhetherDailyQuizSolvedResponse;
import com.shy_polarbear.server.domain.quiz.service.QuizService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shy_polarbear.server.global.common.dto.ApiResponse.success;

@Slf4j
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/daily")
    public ApiResponse<QuizCardResponse> getDailyQuiz(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        QuizCardResponse response = quizService.getDailyQuiz(principalDetails.getUser().getId());
        return success(response);
    }

    @GetMapping("/daily/whether-solved")
    public ApiResponse<WhetherDailyQuizSolvedResponse> getWhetherDailyQuizSolved(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        WhetherDailyQuizSolvedResponse response = quizService.getWhetherDailyQuizSolved(principalDetails.getUser().getId());
        return success(response);
    }

    @GetMapping("/review")
    public ApiResponse<PageResponse<QuizCardResponse>> getReviewQuizzes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(required = false, defaultValue = BusinessLogicConstants.REVIEW_QUIZ_LIMIT_PARAM_DEFAULT_VALUE) int limit
    ) {
        PageResponse<QuizCardResponse> pageResponse = quizService.getReviewQuizzes(principalDetails.getUser().getId(), limit);
        return success(pageResponse);
    }

    @PostMapping("/ox/{quizId}/score")
    public ApiResponse<OXQuizScoreResponse> scoreOXQuiz(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable long quizId,
            @Valid @RequestBody OXQuizScoreRequest requestBody
    ) {
        OXQuizScoreResponse response = quizService.scoreOXQuizSubmission(principalDetails.getUser().getId(), quizId, requestBody);
        return success(response);
    }

    @PostMapping("/multiple-choice/{quizId}/score")
    public ApiResponse<MultipleChoiceQuizScoreResponse> scoreMultipleChoiceQuiz(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable long quizId,
            @Valid @RequestBody MultipleChoiceQuizScoreRequest requestBody
    ) {
        MultipleChoiceQuizScoreResponse response = quizService.scoreMultipleQuizSubmission(principalDetails.getUser().getId(), quizId, requestBody);
        return success(response);
    }

}
