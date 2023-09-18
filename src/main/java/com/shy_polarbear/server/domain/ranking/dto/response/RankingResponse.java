package com.shy_polarbear.server.domain.ranking.dto.response;

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
    private Integer winningPercent;
}
