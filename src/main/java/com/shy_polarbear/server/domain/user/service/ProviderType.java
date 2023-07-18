package com.shy_polarbear.server.domain.user.service;

import lombok.Getter;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

@Getter
public enum ProviderType {
    KAKAO("Kakao");

    private String value;

    ProviderType(String value) {
        this.value = value;
    }
}
