package com.shy_polarbear.server.global.common.constants;

public class GlobalConstants {
    public static final String[] permittedUrls = {
            "/api/auth/join/**",
            "/api/auth/login/**",
            "/api/auth/reissue/**",
            "/api/user/duplicate-nickname",
            "/api/prize/**",
            "/api/quiz", "/api/quiz",
            "/api/auth/test",
            "/api/health"
    };

    public static String[] permittedUrlsWithPostMethod = {
            "/api/images"
    };

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
