package com.shy_polarbear.server.domain.notification.controller;

import com.shy_polarbear.server.domain.notification.dto.NotificationResponse;
import com.shy_polarbear.server.domain.notification.service.NotificationService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/users/me")
    public ApiResponse<List<NotificationResponse>> getMyNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(notificationService.getMyNotifications(principalDetails.getUser().getId()));
    }

}
