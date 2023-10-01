package com.shy_polarbear.server.domain.ranking.entity;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("0")
    private Integer totalRankingPoint;

    public static Ranking createRanking(User user) {
        return new Ranking(user);
    }

    public Ranking(User user) {
        this.user = user;
    }

    public void updateTotalRankingPoint(int totalRankingPoint) {
        this.totalRankingPoint = totalRankingPoint;
    }

    public int calculateWinningPercent() {
        return 0;
    }

}
