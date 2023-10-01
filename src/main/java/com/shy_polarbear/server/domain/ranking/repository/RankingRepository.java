package com.shy_polarbear.server.domain.ranking.repository;

import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long>, RankingRepositoryCustom {
}
