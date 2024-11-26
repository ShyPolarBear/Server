package com.shy_polarbear.server.domain.auth.repository;

import com.shy_polarbear.server.domain.auth.entity.RdbRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RdbRefreshTokenRepository extends JpaRepository<RdbRefreshToken, Long> {

    Optional<RdbRefreshToken> findByRefreshToken(String refreshToken);
    Optional<RdbRefreshToken> findByUserId(Long userId);
}
