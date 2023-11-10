package com.shy_polarbear.server.domain.notification.dto;

import com.shy_polarbear.server.domain.notification.model.Notification;
import com.shy_polarbear.server.domain.notification.model.NotificationType;
import lombok.Builder;

@Builder
public record NotificationResponse (
        Long notificationId,
        NotificationType notificationType,
        String target,
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
                .target(target)
                .redirectTargetId(notification.getRedirectTargetId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdDate(notification.getCreatedDate())   // TODO: 포맷 변경 YYYY-MM-DD HH:mm
                .isRead(notification.isRead())
                .build();
    }
}
