package com.shy_polarbear.server.domain.point.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.point.model.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.shy_polarbear.server.domain.point.model.QPoint.point;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Point> findUserPointsAfterResetDate(Long userId, String resetDate) {
        JPAQuery<Point> query = queryFactory
                .selectFrom(point)
                .where(
                        point.user.id.eq(userId),
                        point.createdDate.goe(resetDate)
                );
        return query.fetch();
    }
}
