package com.shy_polarbear.server.global.common.util.profiles;

import com.shy_polarbear.server.global.common.constants.ProfileConstants;

import java.util.Objects;

public class CustomProfileUtils {
    public static void validateIsProfileNullOrTest() {
        String profile = System.getProperty("spring.profiles.active");

        if (Objects.isNull(profile)) return;

        if (profile.equals(ProfileConstants.TEST)) return;

        if (profile.equals(ProfileConstants.LOCAL) || profile.equals(ProfileConstants.DEV) || profile.equals(ProfileConstants.PROD)) {
            throw new RuntimeException("INVALID PROFILE");
        }
    }
}
