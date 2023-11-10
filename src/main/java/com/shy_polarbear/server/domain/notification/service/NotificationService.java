package com.shy_polarbear.server.domain.notification.service;

import com.shy_polarbear.server.domain.notification.model.Notification;
import com.shy_polarbear.server.domain.notification.repository.NotificationRepository;
import com.shy_polarbear.server.domain.notification.vo.NotificationParams;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
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

}
