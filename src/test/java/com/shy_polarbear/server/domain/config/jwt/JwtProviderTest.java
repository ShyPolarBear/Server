package com.shy_polarbear.server.domain.config.jwt;


import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.config.jwt.JwtProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private final UserRole role = UserRole.ROLE_USR;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .nickName(nickName)
                .email(email)
                .profileImage(profileImage)
                .phoneNumber(phoneNumber)
                .role(role)
                .providerId("1111")
                .build();
        userRepository.save(user);
    }
    @Autowired private JwtProvider jwtProvider;

    @DisplayName("createAccessToken 메서드는 User가 주어지면, accessToken을 리턴한다.")
    @Test
    void createAccessToken() {
        String accessToken = jwtProvider.createAccessToken(user);
        Assertions.assertThat(accessToken).isNotEmpty();
        assertThatCode(() -> jwtProvider.isValidateAccessToken(accessToken)).doesNotThrowAnyException();
    }
}