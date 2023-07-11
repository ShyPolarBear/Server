package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void checkDuplicationNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new UserException(ExceptionStatus.NICKNAME_DUPLICATION);
        }

    }

    public void save(User user) {
        userRepository.save(user);
    }
}
