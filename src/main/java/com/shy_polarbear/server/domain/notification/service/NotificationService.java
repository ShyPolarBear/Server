package com.shy_polarbear.server.domain.notification.service;

import com.google.api.gax.rpc.StatusCode;
import com.shy_polarbear.server.domain.notification.dto.NotificationReadResponse;
import com.shy_polarbear.server.domain.notification.dto.NotificationResponse;
import com.shy_polarbear.server.domain.notification.exception.NotificationException;
import com.shy_polarbear.server.domain.notification.model.Notification;
import com.shy_polarbear.server.domain.notification.repository.NotificationRepository;
import com.shy_polarbear.server.domain.notification.vo.NotificationParams;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {
    private final FirebaseCloudMessagingService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional
    public void pushMessage(NotificationParams params) {
        User receiver = userService.getUser(params.receiver().getId());

        fcmService.sendPushMessage(receiver.getFcmToken(), params);
        Notification notification = Notification.createNotification(
                receiver,
                params.title(),
                params.content(),
                params.notificationType(),
                params.redirectTargetId());

        notificationRepository.save(notification);
        receiver.addNotification(notification);
    }

    // 내 알림 리스트 조회
    public List<NotificationResponse> getMyNotifications(Long userId) {
        List<Notification> notificationList = notificationRepository.findAllByReceiverId(1L);
        return notificationList.stream()
                .map(NotificationResponse::of)
                .toList();
    }

    // 알림 읽음 처리
    @Transactional
    public NotificationReadResponse readNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new NotificationException(ExceptionStatus.NOT_FOUND_NOTIFICATION));
        notification.read();

        return NotificationReadResponse.of(notification);
    }

}
