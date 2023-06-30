package com.shy_polarbear.server.domain.point.entity;

import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int totalPoint;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    public void updateScore(Point point) {
        this.totalPoint += point.getValue();
        updateRanking();
    }

    private void updateRanking() {
        //TODO 랭킨 업데이트 로직 추가
    }

}
