package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.config.jwt.JwtDto;
import com.shy_polarbear.server.domain.config.jwt.RefreshToken;
import com.shy_polarbear.server.domain.config.jwt.RefreshTokenRepository;
import com.shy_polarbear.server.domain.config.security.PrincipalDetails;
import com.shy_polarbear.server.domain.user.dto.*;
import com.shy_polarbear.server.domain.user.dto.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.LoginRequest;
import com.shy_polarbear.server.domain.user.exception.AuthException;
import com.shy_polarbear.server.domain.config.jwt.JwtProvider;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final KakaoProvider kakaoProvider;
    private UserDetailsService userDetailsService;

    //받은 카카오 access token으로 회원가입한 회원인지 판단
    public JwtDto authLogin(@RequestBody LoginRequest loginRequest) {
        KakaoProvider.KakaoUserInfo userInfoByAccessToken = kakaoProvider.getUserInfoByAccessToken(loginRequest.getOauthAccessToken());

        //DB에 유저가 있다면 로그인 처리, 없다면 회원가입을 위한 sign token 발급
        String providerId = userInfoByAccessToken.getId();
        Optional<User> byProviderId = userRepository.findByProviderId(providerId);

        JwtDto issue;
        if (byProviderId.isPresent()) {
            User existUser = byProviderId.get();

            // 인증 객체 생성
            Authentication  authentication = new UsernamePasswordAuthenticationToken(providerId, existUser.getPassword());
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            User loginUser = principal.getUser();
            issue = jwtProvider.issue(loginUser);
        } else {
            String signToken = jwtProvider.createSignToken(providerId);
            throw new AuthException(ExceptionStatus.NEED_TO_JOIN, signToken);
        }
        return issue;
    }

    //일반 유저 회원가입 - 로그인 시 발급 받은 signToken 필요
    public JwtDto join(JoinRequest joinRequest) {

        //sign token 유효성 검증
        String signToken = joinRequest.getSignToken();
        if (!jwtProvider.isValidateToken(signToken)) {
            throw new AuthException(ExceptionStatus.SIGNUP_TOKEN_ERROR);
        }

        //닉네임 중복 검증
        checkDuplicationNickName(joinRequest.getNickName());

        //이미 가입된 유저인지 확인

        //유저 저장
        //User joinUser = User.createUser(joinRequest.getNickName(), joinRequest.getEmail(), joinRequest.getProfileImage(), joinRequest.getPhoneNumber(), UserRole.ROLE_USR);
        //userRepository.save(joinUser);

        //토큰 발급


        return null;

    }

    public void checkDuplicationNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new UserException(ExceptionStatus.NICKNAME_DUPLICATION);
        }
    }

    public User findUserByAccessToken(String accessToken) {
        if (!jwtProvider.isValidateToken(accessToken)) {
            throw new AuthException(ExceptionStatus.INVALID_TOKEN);
        }
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
