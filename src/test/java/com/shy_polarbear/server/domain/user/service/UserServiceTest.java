package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.user.dto.user.response.DuplicateNicknameResponse;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.infra.ProviderType;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("UserService 클래스")
class UserServiceTest {

    private User user1, user2;
    private final String nickName1 = "노을";
    private final String nickName2 = "진욱";
    private final String email = "chi6465618@naver.com";
    private final String profileImage = "";
    private final String phoneNumber = "01093926465";
    private final UserRole userRole = UserRole.ROLE_USR;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user1 = User.createUser(nickName1, email, profileImage, phoneNumber, userRole, "1", ProviderType.KAKAO.value, passwordEncoder);
        user2 = User.createUser(nickName2, email, profileImage, phoneNumber, userRole, "1", ProviderType.KAKAO.value, passwordEncoder);
        userRepository.save(user1);
    }

    @DisplayName("닉네임이 중복되지 않는다면 DuplicateNicknameResponse을 반환한다.")
    @Test
    void checkDuplicateNickName() {
        DuplicateNicknameResponse response = userService.checkDuplicateNickName("이진욱");
        Assertions.assertThat(response.getAvailable()).isTrue();
    }

    @DisplayName("닉네임이 중복된다면 DuplicateNicknameException을 던진다.")
    @Test
    void checkDuplicateNickName_exception() {
        assertThrows(new DuplicateNicknameException
                (ExceptionStatus.NOT_FOUND_USER, new DuplicateNicknameResponse(false)).getClass(),
                () -> userService.checkDuplicateNickName(user1.getNickName()));
    }

    @DisplayName("save() 메서드는 유저를 저장하고 userId를 반환한다.")
    @Test
    void save() {
        Long userId = userService.saveUser(user2);
        Assertions.assertThat(userId).isEqualTo(user2.getId());
    }

    @DisplayName("이미 가입한 유저라면 UserException을 던진다. ")
    @Test
    void checkDuplicateUser() {
        assertThrows(new UserException(ExceptionStatus.USER_ALREADY_EXISTS).getClass(),
                () -> userService.checkDuplicateUser(user1.getProviderId()));
    }

}