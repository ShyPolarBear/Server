package com.shy_polarbear.server.domain.auth.service;

import com.shy_polarbear.server.domain.auth.dto.*;
import com.shy_polarbear.server.domain.auth.exception.AuthException;
import com.shy_polarbear.server.domain.auth.infrastructure.JwtProvider;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    //일반 유저 회원가입
    public TokenResponse join(JoinRequest joinRequest) {

        //sign token 유효성 검증
        String signToken = joinRequest.getSignToken();
        jwtProvider.validateToken(signToken);

        //닉네임 중복 검증
        userService.checkDuplicationNickName(joinRequest.getNickName());

        //유저 저장
        User joinUser = User.createUser(joinRequest.getNickName(), joinRequest.getEmail(), joinRequest.getProfileImage(), joinRequest.getPhoneNumber(), UserRole.USR);
        userService.save(joinUser);

        //토큰 발급
        TokenResponse tokenResponse = oAuthLogin(LoginRequest.from(joinUser));
//        joinUser.updateRefreshToken();
        return null;
    }


    public TokenResponse oAuthLogin(LoginRequest loginRequest) {
        return null;
    }

    public User findUserByAccessToken(String accessToken) {
        jwtProvider.validateToken(accessToken);
        String userId = jwtProvider.getAccessTokenPayload(accessToken);

        Optional<User> userAble = userRepository.findById(Long.parseLong(userId));
        if (userAble.isPresent()) {
            User user = userAble.get();
            if (user.isSameAccessToken(accessToken)) {
                return user;
            }
        }
        throw new AuthException(ExceptionStatus.INVALID_TOKEN);
    }

    // Access Token 만료 되었을 경우, refresh Token 으로 재발급
    public TokenResponse accessTokenByRefreshToken(String refreshToken) {
        return null;
    }

    public LogoutResponse logOut() {
        return null;
    }
}
