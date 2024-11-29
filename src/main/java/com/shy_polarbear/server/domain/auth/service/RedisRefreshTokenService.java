package com.shy_polarbear.server.domain.auth.service;

import com.shy_polarbear.server.domain.auth.entity.RedisRefreshToken;
import com.shy_polarbear.server.domain.auth.repository.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisRefreshTokenService implements RefreshTokenService{
    private final RedisRefreshTokenRepository refreshTokenRepository;

    @Override
    public void save(Long userId, String refreshToken) {
        refreshTokenRepository.save(RedisRefreshToken.of(userId, refreshToken));
    }

    @Override
    public Optional<String> findByUserId(Long userId) {
        return refreshTokenRepository.findById(String.valueOf(userId)).map(RedisRefreshToken::getRefreshToken);
    }

    @Override
    public Optional<String> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(RedisRefreshToken::getRefreshToken);
    }

    @Override
    public void deleteTokenByUserId(Long userId) {
        refreshTokenRepository.deleteById(String.valueOf(userId));;
    }
}
