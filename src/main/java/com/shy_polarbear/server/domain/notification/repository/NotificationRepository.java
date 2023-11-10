package com.shy_polarbear.server.domain.notification.repository;

import com.shy_polarbear.server.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
