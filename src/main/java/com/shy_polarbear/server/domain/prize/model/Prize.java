package com.shy_polarbear.server.domain.prize.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prize_id")
    private Long id;
    private String name;
    private String prizeImage;
    private PrizeStatus prizeStatus = PrizeStatus.NOT_GIVEN;

    @Builder
    private Prize(String name, String prizeImage) {
        this.name = name;
        this.prizeImage = prizeImage;
    }

    public static Prize createPrize(String name, String prizeImage) {
        return Prize.builder()
                .name(name)
                .prizeImage(prizeImage)
                .build();
    }
}
