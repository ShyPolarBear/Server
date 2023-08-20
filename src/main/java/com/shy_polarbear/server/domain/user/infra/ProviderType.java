package com.shy_polarbear.server.domain.user.infra;



public enum ProviderType {
    KAKAO("Kakao");
    public final String value;

    ProviderType(String value) {
        this.value = value;
    }
}
