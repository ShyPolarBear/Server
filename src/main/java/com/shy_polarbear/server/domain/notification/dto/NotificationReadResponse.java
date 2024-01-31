package com.shy_polarbear.server.domain.notification.dto;

import com.shy_polarbear.server.domain.notification.model.Notification;
import lombok.Builder;

@Builder
public record NotificationReadResponse (
        Long notificationId
){
    public static NotificationReadResponse of(Notification notification) {
        return NotificationReadResponse.builder()
                .notificationId(notification.getId())
                .build();
    }
}
