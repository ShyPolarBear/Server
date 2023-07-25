package com.shy_polarbear.server.domain.user.repository;

import com.shy_polarbear.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickName(String nickName);

    boolean existsByNickName(String nickname);

    boolean existsByProviderId(String providerId);

    Optional<User> findByProviderId(String providerId);
}
