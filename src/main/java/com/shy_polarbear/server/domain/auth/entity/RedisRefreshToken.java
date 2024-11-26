package com.shy_polarbear.server.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1209600)
public class RedisRefreshToken {
    @Id
    @NotNull
    private Long userId;

    @Indexed
    @NotNull
    private String refreshToken;

    public static RedisRefreshToken of(Long userId, String refreshToken) {
        return RedisRefreshToken
                .builder()
                .refreshToken(refreshToken)
                .userId(userId)
                .build();
    }

}
