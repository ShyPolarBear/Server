package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RankingService {
    public PageResponse<RankingResponse> findRankingList(Long lastRankingId, Integer limit) {
        return null;
    }

    public RankingResponse findMyRanking() {
        return null;
    }
}
