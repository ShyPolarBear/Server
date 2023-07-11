package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.config.jwt.JwtDto;
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
    public JwtDto join(JoinRequest joinRequest) {

        //sign token 유효성 검증
        String signToken = joinRequest.getSignToken();
        jwtProvider.validateToken(signToken);

        //닉네임 중복 검증
        checkDuplicationNickName(joinRequest.getNickName());

        //이미 가입된 유저인지 확인

        //유저 저장
        User joinUser = User.createUser(joinRequest.getNickName(), joinRequest.getEmail(), joinRequest.getProfileImage(), joinRequest.getPhoneNumber(), UserRole.USR);
        userRepository.save(joinUser);

        //토큰 발급
        JwtDto jwtResponse = authLogin(LoginRequest.from(joinUser));

        return jwtResponse;
    }

    public void checkDuplicationNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new UserException(ExceptionStatus.NICKNAME_DUPLICATION);
        }
    }

    public JwtDto authLogin(LoginRequest loginRequest) {
        return null;
    }

    public User findUserByAccessToken(String accessToken) {
        jwtProvider.validateToken(accessToken);
        String userId = jwtProvider.getAccessTokenPayload(accessToken);

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_TOKEN));
        return user;
    }

    public LogoutResponse logOut(TokenRequest tokenRequest) {
        User findUser = findUserByAccessToken(tokenRequest.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByUser(findUser)
                .orElseThrow(() -> new AuthException(ExceptionStatus.NOT_FOUND_REFRESH_TOKEN));
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();
        return new LogoutResponse();
    }

    public JwtDto reissue(String refreshToken) {
        return jwtProvider.reissue(refreshToken);
    }
}
