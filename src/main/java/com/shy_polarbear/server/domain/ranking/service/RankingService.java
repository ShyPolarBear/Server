package com.shy_polarbear.server.domain.ranking.service;

import com.shy_polarbear.server.domain.ranking.dto.response.RankingResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
@Transactional
public class RankingService {
    public RankingResponse findMyRanking() {

        //랭킹 정산
        //포인트로 당첨 확률 계산
        //당첨확률로 정렬해서 랭킹 가져오기
        return null;
    }
}
