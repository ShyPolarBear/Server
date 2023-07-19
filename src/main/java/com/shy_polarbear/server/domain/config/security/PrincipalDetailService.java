package com.shy_polarbear.server.domain.config.security;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PrincipalDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException {
        User principal = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 providerId 사용자를 찾을 수 없습니다. : " + providerId));
        return new PrincipalDetails(principal);
    }
}
