package com.shy_polarbear.server.domain.ranking.entity;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("0")
    private Integer rankingPoint;

    @ColumnDefault("0")
    private Double winningPercent;

    private Integer rankValue;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean visibility = false;

    public static Ranking createRanking(User user) {
        return new Ranking(user);
    }

    public Ranking(User user) {
        this.user = user;
    }

    public void updateTotalRankingPoint(int rankingPoint) {
        this.rankingPoint = rankingPoint;
        this.visibility = true;
    }

    public void updateRankValue(int rankValue) {
        this.rankValue = rankValue;
    }

    public void calculateWinningPercent(double totalPoint) {
        this.winningPercent = rankingPoint / totalPoint * 100;
    }
}
