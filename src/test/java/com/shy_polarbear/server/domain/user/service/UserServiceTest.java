package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.user.dto.user.response.DuplicateNicknameResponse;
import com.shy_polarbear.server.domain.user.dto.user.response.UserInfoResponse;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.ProviderType;
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

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DisplayName("UserService 클래스")
class UserServiceTest {
    private User user1, user2;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user1 = User.createUser("노을", "chi6465618@naver.com", null, "01093926465", UserRole.ROLE_USR, "1", ProviderType.KAKAO, passwordEncoder);
        user2 = User.createUser("진욱", "1234@naver.com", null, "01071215469", UserRole.ROLE_USR, "2", ProviderType.KAKAO, passwordEncoder);
        userRepository.save(user1); //user1은 가입시킨다.
    }

    @DisplayName("닉네임이 중복되지 않는다면 DuplicateNicknameResponse을 반환한다.")
    @Test
    void checkDuplicateNickName() {
        DuplicateNicknameResponse response = userService.checkDuplicateNickName("중복될 수 없는 닉네임");
        Assertions.assertThat(response.getAvailable()).isTrue();
    }

    @DisplayName("닉네임이 중복된다면 DuplicateNicknameException을 던진다.")
    @Test
    void checkDuplicateNickName_exception() {
        DuplicateNicknameException exception = assertThrows(DuplicateNicknameException.class, () -> userService.checkDuplicateNickName(user1.getNickName()));
        assertThat(exception.getExceptionStatus()).isEqualTo(ExceptionStatus.NICKNAME_DUPLICATION);
    }

    @DisplayName("이미 가입한 유저라면 UserException을 던진다. ")
    @Test
    void checkDuplicateUser() {
        assertThrows(new UserException(ExceptionStatus.USER_ALREADY_EXISTS).getClass(),
                () -> userService.checkDuplicateUser(user1.getProviderId()));
    }

    @DisplayName("saveUser() 메서드는 유저를 저장하고 userId를 반환한다.")
    @Test
    void save() {
        Long userId = userService.saveUser(user2);
        Assertions.assertThat(userId).isEqualTo(user2.getId());
    }

    @DisplayName("findUserInfo() 메서드는 유저의 정보를 반환한다.")
    @Test
    void findUserInfo() {
        UserInfoResponse userInfo = userService.findUserInfo(user1);
        Assertions.assertThat(userInfo.getEmail()).isEqualTo(user1.getEmail());
        Assertions.assertThat(userInfo.getNickName()).isEqualTo(user1.getNickName());
        Assertions.assertThat(userInfo.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
        Assertions.assertThat(userInfo.getProfileImage()).isEqualTo("");
    }
}