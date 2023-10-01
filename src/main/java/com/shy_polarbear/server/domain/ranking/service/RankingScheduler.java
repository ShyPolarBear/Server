package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.point.repository.PointRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.util.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RankingScheduler {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    // 매일 8시 랭킹의 포인트 합계 업데이트
    // 유저의 이번달 1일 8시 이후 포인트 합계 == 랭킹의 포인트 합계
    @Scheduled(cron = BusinessLogicConstants.RANKING_UPDATE_DATE)
    public void updateRankingPoint() {

        userRepository.findAll().stream()
                .map(user -> calculatePointSumThisMonth(user));

    }

    // 유저의 이번달 1일 8시 이후 포인트 합계 구하기
    private int calculatePointSumThisMonth(User user) {
        int sum = 0;
        return sum;
    }

    private String getFirstDayOfMonth8AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfThisMonth  = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 8, 0);
        return LocalDateTimeUtils.convertToString(firstDayOfThisMonth);
    }
}
