package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.ranking.exception.RankingException;
import com.shy_polarbear.server.domain.ranking.repository.RankingRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;

    public PageResponse<RankingResponse> findRankingList(Long lastRankingId, Integer limit) {
        Integer lastRankingPoint = null;
        if (lastRankingId != null) {
            Ranking lastRanking = rankingRepository.findById(lastRankingId)
                    .orElseThrow(() -> new RankingException(ExceptionStatus.NOT_FOUND_RANKING));
            lastRankingPoint = lastRanking.getRankingPoint();
        }

        Slice<RankingResponse> rankingResponses = rankingRepository.findRankingList(lastRankingId, lastRankingPoint, limit)
                .map(ranking -> RankingResponse.of(ranking));
        return PageResponse.of(rankingResponses, rankingResponses.stream().count());
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
