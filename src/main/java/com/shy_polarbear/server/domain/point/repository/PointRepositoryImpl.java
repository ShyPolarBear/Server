package com.shy_polarbear.server.domain.point.repository;

import com.shy_polarbear.server.domain.point.model.Point;

import java.util.List;

public class PointRepositoryImpl implements PointRepositoryCustom{
    @Override
    public List<Point> findUserPointsForThisMonth(Long userId, String thisMonth) {
        return null;
    }
}
