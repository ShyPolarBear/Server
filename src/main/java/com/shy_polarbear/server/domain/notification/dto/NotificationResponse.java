package com.shy_polarbear.server.domain.notification.dto;

import com.shy_polarbear.server.domain.notification.model.Notification;
import com.shy_polarbear.server.domain.notification.model.NotificationType;
import lombok.Builder;

@Builder
public record NotificationResponse (
        Long notificationId,
        NotificationType notificationType,
        String redirectTarget,
        Long redirectTargetId,
        String title,
        String content,
        String createdDate,
        boolean isRead
){
    public static NotificationResponse of(Notification notification) {
        NotificationType notificationType = notification.getNotificationType();
        String target = notificationType.getRedirectTargetClass().getSimpleName();

        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .notificationType(notificationType)
                .redirectTarget(target)
                .redirectTargetId(notification.getRedirectTargetId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdDate(notification.getCreatedDate())
                .isRead(notification.isRead())
                .build();
    }
}
