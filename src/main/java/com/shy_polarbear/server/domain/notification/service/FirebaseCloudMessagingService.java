package com.shy_polarbear.server.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.shy_polarbear.server.domain.notification.exception.NotificationException;
import com.shy_polarbear.server.domain.notification.vo.FcmMessage;
import com.shy_polarbear.server.domain.notification.vo.Message;
import com.shy_polarbear.server.domain.notification.vo.MessageData;
import com.shy_polarbear.server.domain.notification.vo.NotificationParams;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FirebaseCloudMessagingService {
    private final ObjectMapper objectMapper;
    private final JSONParser jsonParser;

    private static final String FCM_PRIVATE_KEY_PATH = "firebase-private-key.json";
    private static final String FIREBASE_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    // TODO: 요청할 URL 전달 받기
    private static final String PROJECT_ID_URL = "https://fcm.googleapis.com/v1/projects/shypolarbear-199e5\n/messages:send";

    private String getAccessToken() {   // FCM 토큰 발급 받기
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                    .createScoped(List.of(FIREBASE_SCOPE));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new NotificationException(ExceptionStatus.GET_FCM_ACCESS_TOKEN_ERROR);
        }
    }

    private String buildMessage(String targetToken, NotificationParams params) {
        try {
            FcmMessage fcmMessage = FcmMessage.builder()
                    .validate_only(false)
                    .message(Message.builder()
                            .token(targetToken)
                            .data(MessageData.builder()
                                    .title(params.title())
                                    .body(params.content())
                                    .redirectTargetId(String.valueOf(params.redirectTargetId()))
                                    .type(params.notificationType().toString())
                                    .build())
                            .build())
                    .build();

            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            throw new NotificationException(ExceptionStatus.FCM_MESSAGE_JSON_PARSING_ERROR);
        }
    }

    // TODO: Async
    public CompletableFuture<Boolean> sendPushMessage(String fcmToken, NotificationParams notificationParams) {
        String message = buildMessage(fcmToken, notificationParams);
        String accessToken = getAccessToken();


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(PROJECT_ID_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json; UTF-8")
                .post(RequestBody.create(message, MediaType.parse("application/json; charset=urf-8")))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {

            if (!response.isSuccessful() && response.body() != null) {  // 비정상적인 응답일 경우, false 반환
                JSONObject responseBody = (JSONObject) jsonParser.parse(response.body().string());
                String errorMessage = ((JSONObject) responseBody.get("error")).get("message").toString();
                log.warn("FCM [sendPushMessage] okHttp response is not OK : {}", errorMessage);
                return CompletableFuture.completedFuture(false);
            }

            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.warn("FCM [sendPushMessage] I/O Exception : {}", e.getMessage());
            throw new NotificationException(ExceptionStatus.SEND_FCM_PUSH_ERROR);
        }
    }
}
