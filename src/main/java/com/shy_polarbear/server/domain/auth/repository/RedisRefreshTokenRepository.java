package com.shy_polarbear.server.domain.auth.repository;

import com.shy_polarbear.server.domain.auth.entity.RedisRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisRefreshTokenRepository extends CrudRepository<RedisRefreshToken,String> {

    Optional<RedisRefreshToken> findByRefreshToken(String refreshToken);
}
