package com.shy_polarbear.server.domain.config.security;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//spring security 에서 사용하는 인증 객체
@Service
@AllArgsConstructor
@NoArgsConstructor
public class PrincipalDetails implements OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return String.valueOf(user.getRole());
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return null;
    }
}
