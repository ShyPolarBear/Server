package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.point.repository.PointRepository;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.ranking.repository.RankingRepository;
import com.shy_polarbear.server.global.common.util.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RankingScheduler {
    private final PointRepository pointRepository;
    private final RankingRepository rankingRepository;

    // 매일 8시에 랭킹의 포인트 합계 업데이트
    // 유저의 이번달 1일 8시 이후 포인트 합계 == 랭킹의 포인트 합계
    @Scheduled(cron = RANKING_UPDATE_DATE)
    public void updateRanking() {
        log.info("[랭킹 스케줄러 실행]");

        //포인트 업데이트
        rankingRepository.findAll().stream()
                .forEach(ranking -> updateTotalRankingPoint(ranking));
        //순위 계산
        calculateRank();
        //당첨 확률 계산
        calculateWinningPercent();
    }

    private void calculateRank() {
        List<Ranking> rankings = rankingRepository.findRankValueNullableRankingList();
        int rankValue = 1;
        int point = (rankings.size() == 0) ? 0 : rankings.get(0).getRankingPoint();
        for (Ranking ranking : rankings) {
            if (ranking.isSameRankingPoint(point)) {
                ranking.updateRankValue(rankValue);
            } else {
                ranking.updateRankValue(++rankValue);
            }
            point = ranking.getRankingPoint();
        }
    }

    private void calculateWinningPercent() {
        List<Ranking> rankingListWinnable = rankingRepository.findWinnableRankingList();
        double totalPoint = 0;
        for (Ranking ranking : rankingListWinnable) {
            totalPoint += ranking.getRankingPoint();
        }
        for (Ranking ranking : rankingListWinnable) {
            ranking.calculateWinningPercent(totalPoint);
        }
    }

    private void updateTotalRankingPoint(Ranking ranking) {
        Integer rankingPoint = pointRepository.findUserPointsSumAfterResetDate(ranking.getUser().getId(), getRankingPointResetDate());
        rankingPoint = (rankingPoint == null) ? 0 : rankingPoint;
        ranking.updateTotalRankingPoint(rankingPoint);
    }

    private String getRankingPointResetDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfThisMonth  = LocalDateTime.of(now.getYear(), now.getMonth(),
                RANKING_POINT_RESET_DAY, RANKING_POINT_RESET_HOUR, 0);
        return LocalDateTimeUtils.convertToString(firstDayOfThisMonth);
    }
}
