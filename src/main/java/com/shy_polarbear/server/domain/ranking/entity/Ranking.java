package com.shy_polarbear.server.domain.ranking.entity;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Integer totalRankingPoint;
    public void updateTotalRankingPoint(int totalRankingPoint) {
        this.totalRankingPoint = totalRankingPoint;
    }

}
