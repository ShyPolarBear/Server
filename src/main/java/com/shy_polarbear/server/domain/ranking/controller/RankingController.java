package com.shy_polarbear.server.domain.ranking.controller;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import com.shy_polarbear.server.domain.ranking.service.RankingService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.RANKING_LIMIT_PARAM_DEFAULT_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final RankingService rankingService;

    @GetMapping()
    public ApiResponse<PageResponse<RankingResponse>> findRankingList(@RequestParam(required = false) Long lastRankingId,
                                                                      @RequestParam(required = false, defaultValue = RANKING_LIMIT_PARAM_DEFAULT_VALUE) Integer limit) {
        return ApiResponse.success(rankingService.findRankingList(lastRankingId, limit));
    }

    @GetMapping("/me")
    public ApiResponse<RankingResponse> findMyRanking(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(rankingService.findMyRanking());
    }
}
