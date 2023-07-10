package com.shy_polarbear.server.domain.ranking.model;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int winningPercent;
    private int rankValue;

    @Builder
    private Ranking(User user) {
        this.user = user;
    }

    public static Ranking createRanking(User user) {
        return Ranking.builder()
                .user(user)
                .build();
    }
}
