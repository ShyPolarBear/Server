package com.shy_polarbear.server.domain.ranking.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RankingScheduler {

    @Scheduled(cron = "0 0 8 * * *")
    public void updateRankingPoint() {

    }

    @Scheduled(cron = "0 0 8 1 * *")
    public void resetRankingPoint() {

    }
}
