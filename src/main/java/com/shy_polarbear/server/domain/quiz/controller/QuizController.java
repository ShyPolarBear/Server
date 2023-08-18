package com.shy_polarbear.server.domain.quiz.controller;

import com.shy_polarbear.server.domain.quiz.dto.QuizCardResponse;
import com.shy_polarbear.server.domain.quiz.service.QuizService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/daily")
    public ApiResponse<QuizCardResponse> getDailyQuiz(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        QuizCardResponse response = quizService.getDailyQuiz(principalDetails.getUser().getId());
        return ApiResponse.success(response);
    }

}
