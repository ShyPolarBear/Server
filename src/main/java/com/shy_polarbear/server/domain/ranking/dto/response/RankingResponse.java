package com.shy_polarbear.server.domain.ranking.dto.response;

import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class RankingResponse {
    private Integer rank;
    private String profileImage;
    private String nickName;
    private Integer point;
    private Double winningPercent;
    private Long rankingId;

    public static RankingResponse of(Ranking ranking) {
        return RankingResponse.builder()
                .profileImage(ranking.getUser().getProfileImage())
                .nickName(ranking.getUser().getNickName())
                .point(ranking.getRankingPoint())
                .winningPercent(ranking.getWinningPercent())
                .rankingId(ranking.getId())
                .build();
    }
}
