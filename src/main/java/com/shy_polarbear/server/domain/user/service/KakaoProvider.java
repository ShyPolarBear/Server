package com.shy_polarbear.server.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shy_polarbear.server.domain.user.exception.AuthException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoProvider {
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_API_URL;

    //access token 으로 카카오 유저 정보를 얻어온다.
    public KakaoUserInfo getUserInfoByAccessToken(String accessToken) {
        String userInfoJson;
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(KAKAO_API_URL)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            userInfoJson = webClient.get()
                    .retrieve()
                    .bodyToMono(String.class) //응답을 동기적으로 받아오기
                    .block();
        } catch (WebClientResponseException e) {
            throw new AuthException(ExceptionStatus.INVALID_KAKAO_TOKEN);
        }

        //json 유저 정보 객체를 파싱한다.
        return parseUserInfo(userInfoJson);
    }

    private static KakaoUserInfo parseUserInfo(String userInfoJson) {
        String id = null;
        try {
            if (userInfoJson != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode userInfoNode = objectMapper.readTree(userInfoJson);
                id = userInfoNode.get("id").asText();
            }
        } catch (JsonProcessingException e) {
            throw new AuthException(ExceptionStatus.INVALID_KAKAO_TOKEN);
        }
        return new KakaoUserInfo(id);
    }

    @RequiredArgsConstructor
    @Getter
    public static class KakaoUserInfo {
        private final String id;
    }
}
