package com.shy_polarbear.server.domain.auth.service;

import com.shy_polarbear.server.domain.auth.entity.RedisRefreshToken;
import com.shy_polarbear.server.domain.auth.repository.RedisRefreshTokenRepository;
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
    public Optional<RedisRefreshToken> findByUserId(Long userId) {
        return refreshTokenRepository.findById(String.valueOf(userId));
    }

    //토큰 값으로 찾기
    public Optional<RedisRefreshToken> findByUserRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    //토큰 삭제
    public void deleteTokenByUserId(Long userId) {
        refreshTokenRepository.deleteById(String.valueOf(userId));;
    }

}
