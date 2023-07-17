package com.shy_polarbear.server.domain.config.security;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        OAuth2UserInfo oauth2UserInfo = null;
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        if (provider.equals("kakao")) {
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("kakao만 가능합니다.");
        }
        String providerId = oauth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;

        Optional<User> byUserName = userRepository.findByUserName(username);
        if (!byUserName.isPresent()) {
            //회원가입 유도
        }
        return new PrincipalDetails(byUserName.get(), oAuth2User.getAttributes());
    }
}
