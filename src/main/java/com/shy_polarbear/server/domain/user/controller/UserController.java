package com.shy_polarbear.server.domain.user.controller;

import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.*;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<UserFeedsResponse> findAllUserFeeds(@RequestParam(required = false) Long lastFeedId,
                                                           @RequestParam(required = false) Integer limit) {
        limit = (limit == null) ? 10 : limit;
        if (lastFeedId == null) {
            return ApiResponse.success(userService.findUserFeeds(limit));
        }
        return ApiResponse.success(userService.findUserFeedsByCursorId(lastFeedId, limit));
    }

    @GetMapping("/comments/feeds")
    public ApiResponse<UserCommentFeedsResponse> findAllFeedsByUserComment(@RequestParam(required = false) Long lastCommentId,
                                                                           @RequestParam(required = false) Integer limit) {
        limit = (limit == null) ? 10 : limit;
        if (lastCommentId == null) {
            return ApiResponse.success(userService.findUserCommentFeeds(limit));
        }
        return ApiResponse.success(userService.findUserCommentFeedsByCursorId(lastCommentId, limit));
    }
}
