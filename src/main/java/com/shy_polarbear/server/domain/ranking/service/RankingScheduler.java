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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.*;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.RANKING_POINT_RESET_DAY;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.RANKING_POINT_RESET_HOUR;

@Component
@RequiredArgsConstructor
public class RankingScheduler {
    private final PointRepository pointRepository;
    private final RankingRepository rankingRepository;

    // 매일 8시 랭킹의 포인트 합계 업데이트
    // 유저의 이번달 1일 8시 이후 포인트 합계 == 랭킹의 포인트 합계
    // 유저 생성 시 랭킹 생성됨.
    @Scheduled(cron = RANKING_UPDATE_DATE)
    public void updateRanking() {
        rankingRepository.findAll().stream()
                .forEach(ranking -> updateTotalRankingPoint(ranking));
    }

    private void updateTotalRankingPoint(Ranking ranking) {
        int totalRankingPoint = calculateTotalRankingPoint(ranking.getUser().getId());
        ranking.updateTotalRankingPoint(totalRankingPoint);
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
