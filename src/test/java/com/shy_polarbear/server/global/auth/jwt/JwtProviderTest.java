package com.shy_polarbear.server.global.auth.jwt;


import com.shy_polarbear.server.domain.user.model.ProviderType;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.auth.jwt.JwtProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@DisplayName("JwtProvider 클래스")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class JwtProviderTest  {
    private User user;
    private final String nickName = "노을";
    private final String email = "chi6465618@naver.com";
    private final String profileImage = "";
    private final String phoneNumber = "01093926465";
    private final UserRole userRole = UserRole.ROLE_USR;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        user = User.createUser(nickName, email, profileImage, phoneNumber, userRole, "0000", ProviderType.KAKAO, passwordEncoder);
    }

    @DisplayName("createAccessToken 메서드는 User가 주어지면, accessToken을 리턴한다.")
    @Test
    void createAccessToken() {
        String accessToken = jwtProvider.createAccessToken(user);
        String tokenPayload = jwtProvider.getTokenPayload(accessToken);

        Assertions.assertThat(accessToken).isNotEmpty();
        Assertions.assertThat(tokenPayload).isEqualTo("0000");
        assertThatCode(() -> jwtProvider.isValidateAccessToken(accessToken)).doesNotThrowAnyException();
    }

    @DisplayName("createRefreshToken 메서드는 User가 주어지면, refreshToken을 리턴한다.")
    @Test
    void createRefreshToken() {
        String refreshToken = jwtProvider.createRefreshToken(user);
        String tokenPayload = jwtProvider.getTokenPayload(refreshToken);

        Assertions.assertThat(refreshToken).isNotEmpty();
        Assertions.assertThat(tokenPayload).isEqualTo("0000");
        assertThatCode(() -> jwtProvider.isValidateRefreshToken(refreshToken)).doesNotThrowAnyException();
    }
}