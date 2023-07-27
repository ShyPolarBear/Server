package com.shy_polarbear.server.domain.user.controller;

import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.DuplicateNicknameResponse;
import com.shy_polarbear.server.domain.user.dto.user.response.UpdateUserInfoResponse;
import com.shy_polarbear.server.domain.user.dto.user.response.UserFeedsResponse;
import com.shy_polarbear.server.domain.user.dto.user.response.UserInfoResponse;
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

    @PutMapping("/me")
    public ApiResponse<UpdateUserInfoResponse> updateUserInfo(@RequestBody UpdateUserInfoRequest userInfoRequest) {
        return ApiResponse.success(userService.updateUserInfo(userInfoRequest));
    }

    @GetMapping("/duplicate-nickname")
    public ApiResponse<DuplicateNicknameResponse> checkDuplicateNickname(@RequestParam String nickName) {
        return ApiResponse.success(userService.checkDuplicateNickName(nickName));
    }

    @GetMapping("/feeds")
    public ApiResponse<UserFeedsResponse> findUserFeeds(@RequestParam(required = false) String lastFeedId,
                                                        @RequestParam(required = false) String limit) {
        return ApiResponse.success(userService.findUserFeeds(lastFeedId, limit));
    }
}
