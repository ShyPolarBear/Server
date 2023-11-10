package com.shy_polarbear.server.domain.ranking.repository;

import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface RankingRepositoryCustom {

    Slice<Ranking> findRankingList(Long lastRankingId, Integer lastRankingScore, Integer limit);

    List<Ranking> findWinnableRankingList();

    List<Ranking> findRankValueNullableRankingList();

    Optional<Ranking> findUserRanking(Long userId);
}
