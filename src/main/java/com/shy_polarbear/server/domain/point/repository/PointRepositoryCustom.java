package com.shy_polarbear.server.domain.point.repository;

import com.shy_polarbear.server.domain.point.model.Point;

import java.util.List;

public interface PointRepositoryCustom {
    List<Point> findUserPointsAfterResetDate(Long userId, String resetDate);
}
