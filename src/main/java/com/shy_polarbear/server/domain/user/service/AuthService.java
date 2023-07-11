package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.config.jwt.RefreshToken;
import com.shy_polarbear.server.domain.config.jwt.RefreshTokenRepository;
import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.exception.AuthException;
import com.shy_polarbear.server.domain.config.jwt.JwtProvider;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;


    //일반 유저 회원가입
    public TokenResponse join(JoinRequest joinRequest) {

        //sign token 유효성 검증
        String signToken = joinRequest.getSignToken();
        jwtProvider.validateToken(signToken);


        //닉네임 중복 검증
        checkDuplicationNickName(joinRequest.getNickName());



        //유저 저장
        User joinUser = User.createUser(joinRequest.getNickName(), joinRequest.getEmail(), joinRequest.getProfileImage(), joinRequest.getPhoneNumber(), UserRole.USR);
        userRepository.save(joinUser);

        //토큰 발급
        TokenResponse tokenResponse = authLogin(LoginRequest.from(joinUser));
//        joinUser.updateRefreshToken();
        return null;
    }

    public void checkDuplicationNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new UserException(ExceptionStatus.NICKNAME_DUPLICATION);
        }
    }

    public TokenResponse authLogin(LoginRequest loginRequest) {
        return null;
    }

    public User findUserByAccessToken(String accessToken) {
        jwtProvider.validateToken(accessToken);
        String userId = jwtProvider.getAccessTokenPayload(accessToken);

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_TOKEN));
        return user;
    }

    // Access Token 만료 되었을 경우, refresh Token 으로 재발급
    public TokenResponse getAccessTokenByRefreshToken(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        RefreshToken newRefresh = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_TOKEN));


        String accessToken = jwtProvider.createAccessToken(newRefresh.getUser());
        newRefresh.getUser().updateAccessToken(accessToken);
        return TokenResponse.from(accessToken, refreshToken);
    }

    public LogoutResponse logOut(TokenRequest tokenRequest) {
        User findUser = findUserByAccessToken(tokenRequest.getAccessToken());
        //refresh token 찾아서 삭제하기
        return null;
    }
}
