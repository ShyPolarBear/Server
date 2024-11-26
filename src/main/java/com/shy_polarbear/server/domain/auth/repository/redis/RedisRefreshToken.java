package com.shy_polarbear.server.domain.auth.repository.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 1209600000)
public class RedisRefreshToken {
    @Id
    @NotNull
    private Long userId;

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
