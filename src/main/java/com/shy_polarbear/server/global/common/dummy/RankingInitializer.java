package com.shy_polarbear.server.global.common.dummy;

import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.ranking.repository.RankingRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.common.constants.ProfileConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;


@Component("RankingInitializer")
@DependsOn("LoginUserInitializer")
@RequiredArgsConstructor
@Slf4j
@Transactional
@Profile({ProfileConstants.LOCAL, ProfileConstants.DEV})
public class RankingInitializer {
    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;


    @PostConstruct
    public void init() {
        if (rankingRepository.count() == 0) {
            createDummyRanking();
            log.info("[RankingInitializer] 유저 수 만큼 랭킹 생성 완료");
        } else {
            log.info("[RankingInitializer] 랭킹 이미 생성");
        }
    }

    private void createDummyRanking() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            rankingRepository.save(Ranking.createRanking(user));
        }
    }
}
