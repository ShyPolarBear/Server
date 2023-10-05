package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.point.model.Point;
import com.shy_polarbear.server.domain.point.repository.PointRepository;
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.ranking.repository.RankingRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.util.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.*;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.RANKING_POINT_RESET_DAY;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.RANKING_POINT_RESET_HOUR;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RankingScheduler {
    private final PointRepository pointRepository;
    private final RankingRepository rankingRepository;

    // 매일 8시 랭킹의 포인트 합계 업데이트
    // 유저의 이번달 1일 8시 이후 포인트 합계 == 랭킹의 포인트 합계
    // 유저 생성 시 랭킹 생성됨.
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
        List<Ranking> rankings = rankingRepository.findAllByOrderByRankingPointDesc();
        int rankValue = 1;
        int point = rankings.size() == 0 ? 0 : rankings.get(0).getRankingPoint();
        for (Ranking ranking : rankings) {
            if (ranking.getRankingPoint() == point) {
                ranking.updateRankValue(rankValue);
            } else {
                ranking.updateRankValue(++rankValue);
            }
            point = ranking.getRankingPoint();
        }
    }

    private void calculateWinningPercent() {
        List<Ranking> rankingListWinnable = rankingRepository.findRankingListWinnable();
        double totalPoint = 0;
        for (Ranking ranking : rankingListWinnable) {
            totalPoint += ranking.getRankingPoint();
        }
        for (Ranking ranking : rankingListWinnable) {
            ranking.calculateWinningPercent(totalPoint);
        }
    }

    private void updateTotalRankingPoint(Ranking ranking) {
        int rankingPoint = calculateTotalRankingPoint(ranking.getUser().getId());
        ranking.updateTotalRankingPoint(rankingPoint);
    }

    // 유저의 이번달 1일 8시 이후 포인트 합계 구하기
    private int calculateTotalRankingPoint(Long userId) {
        int sum = 0;
        List<Point> userPoints = pointRepository.findUserPointsAfterResetDate(userId, getRankingPointResetDate());
        for (Point userPoint : userPoints) {
            sum += userPoint.getValue();
        }
        return sum;
    }

    private String getRankingPointResetDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfThisMonth  = LocalDateTime.of(now.getYear(), now.getMonth(),
                RANKING_POINT_RESET_DAY, RANKING_POINT_RESET_HOUR, 0);
        return LocalDateTimeUtils.convertToString(firstDayOfThisMonth);
    }
}
