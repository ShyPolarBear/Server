package com.shy_polarbear.server.domain.auth.service;

import com.shy_polarbear.server.domain.auth.repository.redis.RedisRefreshToken;
import com.shy_polarbear.server.domain.auth.repository.redis.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisRefreshTokenRepository refreshTokenRepository;

    //토큰 저장
    public void save(RedisRefreshToken token) {
        refreshTokenRepository.save(token);
    }

    //토큰 id로 찾기
    public Optional<RedisRefreshToken> findByUserId(String userId) {
        return refreshTokenRepository.findById(userId);
    }

    //토큰 삭제
    public void deleteTokenByUserId(String userId) {
        refreshTokenRepository.deleteById(userId);;
    }

}
