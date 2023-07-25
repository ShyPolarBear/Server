package com.shy_polarbear.server.domain.user.service;

import lombok.Getter;

@Getter
public enum ProviderType {
    KAKAO("Kakao");

    private String value;

    ProviderType(String value) {
        this.value = value;
    }
}
