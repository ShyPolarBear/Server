package com.shy_polarbear.server.domain.auth.service;

import com.shy_polarbear.server.domain.auth.exception.AuthException;
import com.shy_polarbear.server.domain.auth.dto.response.LogoutResponse;
import com.shy_polarbear.server.domain.auth.jwt.provider.ProviderType;
import com.shy_polarbear.server.domain.user.dto.user.response.DuplicateNicknameResponse;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.domain.auth.jwt.provider.KakaoProvider;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.domain.auth.jwt.JwtDto;
import com.shy_polarbear.server.domain.auth.dto.request.JoinRequest;
import com.shy_polarbear.server.domain.auth.dto.request.SocialLoginRequest;
import com.shy_polarbear.server.domain.auth.jwt.JwtProvider;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.domain.user.entity.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
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
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final KakaoProvider kakaoProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JwtDto authLogin(SocialLoginRequest socialLoginRequest) {
        String providerId = getKakaoProviderId(socialLoginRequest.getSocialAccessToken());
        Optional<User> existUserAble = userRepository.findByProviderId(providerId);
        if (existUserAble.isPresent()) {
            Long userId = authorizeUser(providerId);
            return issue(providerId, userId);
        } else {
            throw new AuthException(ExceptionStatus.NEED_TO_JOIN);
        }
    }

    public JwtDto join(JoinRequest joinRequest) {
        String providerId = getKakaoProviderId(joinRequest.getSocialAccessToken());
        userService.checkDuplicateUser(providerId);
        checkDuplicateNickName(joinRequest.getNickName());

        User joinUser = User.createUser(joinRequest.getNickName(), joinRequest.getEmail(),
                joinRequest.getProfileImage(), joinRequest.getPhoneNumber(),
                UserRole.ROLE_USR, providerId, ProviderType.KAKAO, passwordEncoder);
        userService.saveUser(joinUser);

        Long userId = authorizeUser(providerId);
        return issue(providerId, userId);
    }

    private String getKakaoProviderId(String socialAccessToken) {
        KakaoProvider.KakaoUserInfo userInfo = kakaoProvider.getUserInfoByAccessToken(socialAccessToken);
        String providerId = userInfo.getId();
        return providerId;
    }

    private void checkDuplicateNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new DuplicateNicknameException(ExceptionStatus.NICKNAME_DUPLICATION, new DuplicateNicknameResponse(false));
        }
    }

    private Long authorizeUser(String providerId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(providerId, providerId +"@password");
        Authentication authenticated  = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        PrincipalDetails principal = (PrincipalDetails) authenticated.getPrincipal();
        Long userId = principal.getUser().getId();
        return userId;

    }

    // refresh token 삭제하는 방식 사용
    public LogoutResponse logOut(Long userId) {
        refreshTokenService.deleteTokenByUserId(userId);
        return new LogoutResponse();
    }

    public JwtDto reissue(String refreshToken) {
        if (!jwtProvider.isValidateRefreshToken(refreshToken)) {
            throw new AuthException(ExceptionStatus.INVALID_REFRESH_TOKEN);
        }
        refreshTokenService.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_REFRESH_TOKEN));

        String providerId = jwtProvider.getTokenPayload(refreshToken);
        User user = userRepository.findByProviderId(providerId).orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
        return issue(providerId, user.getId());
    }

    private JwtDto issue(String providerId, Long userId) {
        String accessToken = jwtProvider.createAccessToken(providerId);
        String refreshToken = jwtProvider.createRefreshToken(providerId);
        refreshTokenService.save(userId, refreshToken);
        return JwtDto.from(accessToken, refreshToken);
    }
}
