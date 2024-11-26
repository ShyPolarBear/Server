package com.shy_polarbear.server.domain.auth.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRefreshTokenRepository extends CrudRepository<RefreshToken,String> {
}
