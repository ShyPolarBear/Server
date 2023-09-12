package com.shy_polarbear.server.domain.user.template;

import com.shy_polarbear.server.domain.user.model.ProviderType;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserTemplate {

    public static final Long ID = 1L;
    public static final String NICKNAME = "test";
    public static final String EMAIL = "test@naver.com";
    public static final String PROFILE_IMAGE_URL = "2031239.com";
    public static final String PHONE_NUMBER = "01012341234";
    public static final UserRole ROLE = UserRole.ROLE_USR;
    public static final String PROVIDER_ID = "1";
    public static final ProviderType PROVIDER_TYPE = ProviderType.KAKAO;
    public static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static User createDummyUser() {
        return User.createUser(NICKNAME, EMAIL, PROFILE_IMAGE_URL, PHONE_NUMBER, ROLE, PROVIDER_ID, PROVIDER_TYPE, BCRYPT_PASSWORD_ENCODER);
    }

}
