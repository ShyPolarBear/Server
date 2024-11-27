package com.shy_polarbear.server.domain.auth.service;

import com.shy_polarbear.server.domain.auth.entity.RdbRefreshToken;
import com.shy_polarbear.server.domain.auth.repository.RdbRefreshTokenRepository;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

//@Service
@RequiredArgsConstructor
@Transactional
public class RdbRefreshTokenService implements RefreshTokenService{
    private final RdbRefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    public void save(Long userId, String refreshToken) {
        User user = userService.getUser(userId);
        Optional<RdbRefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);
        if (existingToken.isPresent()) {
            RdbRefreshToken token = existingToken.get();
            token.replace(refreshToken);
        } else {
            refreshTokenRepository.save(new RdbRefreshToken(user, refreshToken));
        }
    }

    @Override
    public Optional<String> findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId).map(RdbRefreshToken::getRefreshToken);

    }

    @Override
    public Optional<String> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(RdbRefreshToken::getRefreshToken);
    }

    @Override
    public void deleteTokenByUserId(Long userId) {
        refreshTokenRepository.findByUserId(userId).ifPresent(refreshTokenRepository::delete);
    }
}
