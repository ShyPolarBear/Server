package com.shy_polarbear.server.domain.notification.repository;

import com.shy_polarbear.server.domain.notification.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryCustom {
    // 특정 알림 조회
    Optional<Notification> findByIdAndReceiverId(Long notificationId, Long receiverId);

    // 내 알림 조회
    List<Notification> findAllByReceiverId(Long userId);

}
