package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RankingService {
    private final UserService userService;

    public PageResponse<RankingResponse> findRankingList(Long lastRankingId, Integer limit) {
        return null;
    }

    public RankingResponse findMyRanking(Long userId) {
        User user = userService.getUser(userId);
        return null;
    }
}
