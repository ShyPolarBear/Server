package com.shy_polarbear.server.domain.user.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DisplayName("User 클래스")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserTest {

    private User user1, user2;
    private final String nickName = "노을";
    private final String email = "chi6465618@naver.com";
    private final String profileImage = "";
    private final String phoneNumber = "01093926465";
    private final UserRole userRole = UserRole.ROLE_USR;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        user1 = User.createUser(nickName, email, profileImage, phoneNumber, userRole, "1111", ProviderType.KAKAO, passwordEncoder);
        user2 = User.createUser(nickName, email, profileImage, phoneNumber, userRole, "2222", ProviderType.KAKAO, passwordEncoder);
    }

    @DisplayName("User 객체가 빌더 패턴으로 생성된다.")
    @Test
    void createUser() {
        assertThat(user1).isNotNull();
        assertThat(user1.getNickName()).isEqualTo(nickName);
        assertThat(user1.getUserStatus()).isEqualTo(UserStatus.ENGAGED);
    }

    @DisplayName("user1이 user2를 차단한다.")
    @Test
    void blockUser() {
        user1.blockUser(user2);
        assertThat(user1.getBlockedUsers().size()).isEqualTo(1);
    }

}