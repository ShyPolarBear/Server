package com.shy_polarbear.server.domain.notification.vo;

import lombok.Builder;

@Builder
public record Message(
        String token,
        MessageData data
) {
}
