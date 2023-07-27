package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.user.dto.auth.response.LogoutResponse;
import com.shy_polarbear.server.domain.user.dto.user.response.DuplicateNicknameResponse;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.global.auth.jwt.JwtDto;
import com.shy_polarbear.server.global.auth.jwt.RefreshToken;
import com.shy_polarbear.server.global.auth.jwt.RefreshTokenRepository;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.domain.user.dto.auth.request.JoinRequest;
import com.shy_polarbear.server.domain.user.dto.auth.request.SocialLoginRequest;
import com.shy_polarbear.server.domain.user.exception.AuthException;
import com.shy_polarbear.server.global.auth.jwt.JwtProvider;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    //DB에 유저가 있다면 로그인 처리, 없다면 회원가입 유도
    public JwtDto authLogin(SocialLoginRequest socialLoginRequest) {
        KakaoProvider.KakaoUserInfo userInfoByAccessToken = kakaoProvider.getUserInfoByAccessToken(socialLoginRequest.getSocialAccessToken());
        String providerId = userInfoByAccessToken.getId();

        Optional<User> existUserAble = userRepository.findByProviderId(providerId);
        if (existUserAble.isPresent()) {
            return authorizeUser(providerId);
        } else {
            throw new AuthException(ExceptionStatus.NEED_TO_JOIN);
        }
    }

    public JwtDto join(JoinRequest joinRequest) {
        //카카오 provider id 가져오기
        String socialAccessToken = joinRequest.getSocialAccessToken();
        KakaoProvider.KakaoUserInfo userInfo = kakaoProvider.getUserInfoByAccessToken(socialAccessToken);
        String providerId = userInfo.getId();

        //이미 가입된 유저인지 확인
        userService.checkDuplicateUser(providerId);

        //닉네임 중복 검증
        userService.checkDuplicateNickName(joinRequest.getNickName());

        //유저 저장
        User joinUser = User.createUser(joinRequest.getNickName(), joinRequest.getEmail(),
                joinRequest.getProfileImage(), joinRequest.getPhoneNumber(),
                UserRole.ROLE_USR, providerId, ProviderType.KAKAO.getValue(), passwordEncoder);
        userService.saveUser(joinUser);

        //로그인 (유저 인증)
        JwtDto issuedToken = authorizeUser(providerId);
        return issuedToken;
    }

    private JwtDto authorizeUser(String providerId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(providerId, providerId +"@password");
        Authentication authenticated  = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        PrincipalDetails principal = (PrincipalDetails) authenticated.getPrincipal();
        return jwtProvider.issue(principal.getUser());
    }

    // refresh token 삭제하는 방식 사용
    public LogoutResponse logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        User findUser = principal.getUser();
        RefreshToken refreshToken = refreshTokenRepository.findByUser(findUser)
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_REFRESH_TOKEN));
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();
        return new LogoutResponse();
    }

    public JwtDto reissue(String refreshToken) {
        return jwtProvider.reissue(refreshToken);
    }
}
