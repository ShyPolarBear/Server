package com.shy_polarbear.server.domain.notification.vo;

public record PushMessage(
        Long receiverId,
        String title,
        String body
) {
}
