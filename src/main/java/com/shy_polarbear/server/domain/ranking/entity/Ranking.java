package com.shy_polarbear.server.domain.ranking.entity;

import com.shy_polarbear.server.domain.point.entity.Point;
import com.shy_polarbear.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private int rank;

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
