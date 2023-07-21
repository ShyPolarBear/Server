package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.domain.user.dto.user.UserInfoResponse;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> findUserInfo() {
        return ApiResponse.success(userService.findUserInfo());
    }

}
