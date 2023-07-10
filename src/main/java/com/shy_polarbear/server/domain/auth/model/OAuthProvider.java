package com.shy_polarbear.server.domain.auth.model;

import com.shy_polarbear.server.domain.user.model.User;

public abstract class OAuthProvider {

    public User requestLoginUser(String authorizationCode) {
        String accessToken = requestAccessToken(authorizationCode);
        return requestUserInformation(accessToken);
    }


    //access token 발급?
    private String requestAccessToken(String authorizationCode) {
        return null;
    }

    private User requestUserInformation(String accessToken) {
        return null;
    }
}
