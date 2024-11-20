package com.shy_polarbear.server.domain.point.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.shy_polarbear.server.domain.point.entity.QPoint.point;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Integer findUserPointsSumAfterResetDate(Long userId, String resetDate) {
        JPAQuery<Integer> query = queryFactory
                .select(point.value.sum())
                .from(point)
                .where(
                        point.user.id.eq(userId),
                        point.createdDate.goe(resetDate)
                );
        return query.fetchOne();
    }
}
