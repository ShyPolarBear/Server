package com.shy_polarbear.server.domain.point.service;

import com.shy_polarbear.server.domain.point.model.Point;
import com.shy_polarbear.server.domain.point.model.PointType;
import com.shy_polarbear.server.domain.point.repository.PointRepository;
import com.shy_polarbear.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    // 환경 문제 포인트 계산
    @Transactional(propagation = Propagation.MANDATORY)  // TODO: 트랜잭션 전파 고민. 포폴 먹거리
    public int calculateQuizSubmissionPoint(boolean isCorrect, User user) {
        if (isCorrect) {// 정답 여부에 따라 다르게 계산
            pointRepository.save(Point.createPoint(user, PointType.SOLVE_QUIZ));
            return PointType.SOLVE_QUIZ.getValue();
        } else {
            return PointType.NOT_SOLVE_QUIZ.getValue();
        }
    }

    public int calculateQuizSubmissionTimeout() {
        return PointType.NOT_SOLVE_QUIZ.getValue();
    }
}
