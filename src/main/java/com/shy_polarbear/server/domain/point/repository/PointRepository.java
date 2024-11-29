package com.shy_polarbear.server.domain.point.repository;

import com.shy_polarbear.server.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {
}
