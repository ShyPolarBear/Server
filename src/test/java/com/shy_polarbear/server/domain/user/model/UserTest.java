package com.shy_polarbear.server.domain.user.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DisplayName("User 클래스")
class UserTest {

    private User user1, user2;
    private final String nickName = "노을";
    private final String email = "chi6465618@naver.com";
    private final String profileImage = "";
    private final String phoneNumber = "01093926465";
    private final UserRole userRole = UserRole.ROLE_USR;

    @BeforeEach
    void setUp() {
        user1 = User.createUser(nickName, email, profileImage, phoneNumber, userRole);
        user2 = User.createUser(nickName, email, profileImage, phoneNumber, userRole);
    }

    @DisplayName("User 객체가 빌더 패턴으로 생성된다.")
    @Test
    void createUser() {
        assertThat(user1).isNotNull();
        assertThat(user1.getNickName()).isEqualTo(nickName);
        assertThat(user1.getUserStatus()).isEqualTo(UserStatus.ENGAGED);
        assertThat(user1.getIsBlackListUser()).isEqualTo(false);
    }

    @DisplayName("user1이 user2를 차단한다.")
    @Test
    void blockUser() {
        user1.blockUser(user2);
        assertThat(user1.blockedUsers.size()).isEqualTo(1);
    }

}