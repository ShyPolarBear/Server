package com.shy_polarbear.server.domain.notification.vo;

import lombok.Builder;

@Builder
public record FcmMessage (  // 필드명은 FCM 메세지 스펙 따라야 합니다
        Boolean validate_only,
        Message message
){
}
