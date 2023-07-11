package com.shy_polarbear.server.domain.config.jwt;

import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByUser(User user);
}
