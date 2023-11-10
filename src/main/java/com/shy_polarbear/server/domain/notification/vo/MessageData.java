package com.shy_polarbear.server.domain.notification.vo;

import lombok.Builder;

@Builder
public record MessageData(
        String title,
        String body,
        String redirectTargetId,
        String type
) {
}
