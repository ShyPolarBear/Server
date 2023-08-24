package com.shy_polarbear.server.global.common.dummy;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.model.ProviderType;
import com.shy_polarbear.server.global.auth.jwt.JwtDto;
import com.shy_polarbear.server.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Component("LoginUserInitializer")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoginUserInitializer {

    private User user;
    private String nickName = "노을";
    private String email = "chi6465618@naver.com";
    private String profileImage = "";
    private String phoneNumber = "01093926465";
    private UserRole userRole = UserRole.ROLE_USR;
    private String providerId = "0";
    private ProviderType provider = ProviderType.KAKAO;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @PostConstruct
    public void init() {
        createDummyUser();
    }

    private void createDummyUser() {
        Optional<User> userAble = userRepository.findByProviderId(providerId);
        if (userAble.isPresent()) {
            user = userAble.get();
            log.info("유저가 이미 존재하여 더미를 생성하지 않았습니다.");
        } else {
            user = User.createUser(nickName, email, profileImage, phoneNumber, userRole, providerId, provider, passwordEncoder);
            userRepository.save(user);
        }
        JwtDto issue = jwtProvider.issue(user);
        log.info("더미 유저 access token : {}", issue.getAccessToken());
    }
}

