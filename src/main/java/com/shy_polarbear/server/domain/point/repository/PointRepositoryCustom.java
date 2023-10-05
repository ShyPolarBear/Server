package com.shy_polarbear.server.domain.point.repository;

import java.util.List;

public interface PointRepositoryCustom {
    Integer findUserPointsSumAfterResetDate(Long userId, String resetDate);
}
