package com.shy_polarbear.server.domain.ranking.repository;

import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RankingRepositoryCustom {

    Slice<Ranking> findRankingList(Long lastRankingId, Integer lastRankingScore, Integer limit);

    List<Ranking> findRankingListWinnable();
}
