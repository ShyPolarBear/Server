package com.shy_polarbear.server.domain.notification.repository;

import com.shy_polarbear.server.domain.notification.model.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

    List<Notification> findTop30ByReceiverIdOrderByIdDesc(Long receiverId);
}
