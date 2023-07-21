package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.user.dto.user.UserInfoResponse;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.auth.jwt.JwtProvider;
import com.shy_polarbear.server.global.auth.security.SecurityUtils;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    //닉네임 중복 검증
    public void checkDuplicationNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new UserException(ExceptionStatus.NICKNAME_DUPLICATION);
        }
    }

    //이미 가입한 회원 검증
    public void checkDuplicationUser(String providerId) {
        if (userRepository.existsByProviderId(providerId)) {
            throw new UserException(ExceptionStatus.USER_ALREADY_EXISTS);
        }
    }

    public Long save(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public UserInfoResponse findUserInfo() {
        String providerId = SecurityUtils.getLoginUserProviderId();
        User findUser = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));

        return UserInfoResponse.from(findUser);
    }
}
