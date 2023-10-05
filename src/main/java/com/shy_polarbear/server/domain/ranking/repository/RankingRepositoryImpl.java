package com.shy_polarbear.server.domain.ranking.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.global.common.util.CustomSliceExecutionUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shy_polarbear.server.domain.ranking.entity.QRanking.ranking;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.WINNABLE_RANKING_LIMIT;

@Repository
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<Ranking> findRankingList(Long lastRankingId, Integer lastRankingScore, Integer limit) {
        String cursor = generateCursor(lastRankingId, lastRankingScore);
        JPAQuery<Ranking> query = queryFactory
                .selectFrom(ranking)
                .where(
                        lessThanCustomCursor(cursor),
                        ranking.visibility.eq(true)
                )
                .orderBy(
                        ranking.rankingPoint.desc(),
                        ranking.id.desc()
                )
                .limit(CustomSliceExecutionUtils.buildSliceLimit(limit));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), limit);
    }

    @Override
    public List<Ranking> findWinnableRankingList() {
        JPAQuery<Ranking> query = queryFactory
                .selectFrom(ranking)
                .where(
                        ranking.rankValue.loe(WINNABLE_RANKING_LIMIT)
                )
                .orderBy(ranking.rankingPoint.desc());

        return query.fetch();
    }

    @Override
    public List<Ranking> findRankValueNullableRankingList() {
        JPAQuery<Ranking> query = queryFactory
                .selectFrom(ranking)
                .orderBy(
                        ranking.rankingPoint.desc(),
                        ranking.id.desc()
                );
        return query.fetch();
    }

    private String generateCursor(Long lastRankingId, Integer lastRankingScore){
        if (lastRankingId == null || lastRankingScore == null) {
            return null;
        }
        return String.format("%010d", lastRankingScore) + String.format("%019d", lastRankingId);
    }

    private BooleanExpression lessThanCustomCursor(String customCursor){
        if (customCursor == null) {
            return null;
        }
        return StringExpressions
                .lpad(ranking.rankingPoint.stringValue(), 10, '0')
                .concat(StringExpressions.lpad(ranking.id.stringValue(), 19, '0'))
                .lt(customCursor);
    }
}
