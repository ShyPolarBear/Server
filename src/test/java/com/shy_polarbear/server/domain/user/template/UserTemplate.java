package com.shy_polarbear.server.domain.user.template;

import com.shy_polarbear.server.domain.user.model.ProviderType;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserTemplate {

    public static final Long ID = 1L;
    public static final Long OTHER_ID = 2L;
    public static final String NICKNAME = "test";
    public static final String OTHER_NICKNAME = "test2";
    public static final String EMAIL = "test@naver.com";
    public static final String OTHER_EMAIL = "test2@naver.com";
    public static final String PROFILE_IMAGE_URL = "2031239.com";
    public static final String OTHER_PROFILE_IMAGE_URL = "30312391.com";
    public static final String PHONE_NUMBER = "01012341234";
    public static final String OTHER_PHONE_NUMBER = "01012341235";
    public static final UserRole ROLE = UserRole.ROLE_USR;
    public static final UserRole OTHER_ROLE = UserRole.ROLE_USR;
    public static final String PROVIDER_ID = "1";
    public static final String OTHER_PROVIDER_ID = "2";
    public static final ProviderType PROVIDER_TYPE = ProviderType.KAKAO;
    public static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static User createDummyUser() {
        User user = User.createUser(NICKNAME, EMAIL, PROFILE_IMAGE_URL, PHONE_NUMBER, ROLE, PROVIDER_ID, PROVIDER_TYPE, BCRYPT_PASSWORD_ENCODER);
        user.setIdForTest(ID);
        return user;
    }

    public static User createDummyOtherUserA() {
        User user = User.createUser(OTHER_NICKNAME, OTHER_EMAIL, OTHER_PROFILE_IMAGE_URL, OTHER_PHONE_NUMBER, OTHER_ROLE, OTHER_PROVIDER_ID, PROVIDER_TYPE, BCRYPT_PASSWORD_ENCODER);
        user.setIdForTest(OTHER_ID);
        return user;
    }

}
