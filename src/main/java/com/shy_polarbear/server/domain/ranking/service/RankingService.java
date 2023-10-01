package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.ranking.repository.RankingRepository;
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
    private final RankingRepository rankingRepository;

    public PageResponse<RankingResponse> findRankingList(Long lastRankingId, Integer limit) {
        return null;
    }

    public RankingResponse findMyRanking(Long userId) {
        return null;
    }

    public Long saveRanking(User user) {
        Ranking ranking = Ranking.createRanking(user);
        rankingRepository.save(ranking);
        return ranking.getId();
    }
}
