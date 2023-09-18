package com.shy_polarbear.server.domain.ranking.controller;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import com.shy_polarbear.server.domain.ranking.service.RankingService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final RankingService rankingService;

    @GetMapping()
    public ApiResponse<RankingResponse> findMyRanking(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(rankingService.findMyRanking());
    }

}
