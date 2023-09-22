package com.shy_polarbear.server.domain.user.controller;

import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.*;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> findUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(userService.findUserInfo(principalDetails.getUser().getId()));
    }

    @PutMapping("/me")
    public ApiResponse<UpdateUserInfoResponse> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest userInfoRequest,
                                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(userService.updateUserInfo(userInfoRequest, principalDetails.getUser().getId()));
    }

    @GetMapping("/duplicate-nickname")
    public ApiResponse<DuplicateNicknameResponse> checkDuplicateNickname(@RequestParam String nickName) {
        return ApiResponse.success(userService.checkDuplicateNickName(nickName));
    }

    @GetMapping("/feeds")
    public ApiResponse<UserFeedsResponse> findAllUserFeeds(@RequestParam(required = false) Long lastFeedId,
                                                           @RequestParam(required = false) Integer limit,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        limit = (limit == null) ? 10 : limit;
        if (lastFeedId == null) {
            return ApiResponse.success(userService.findUserFeeds(limit, principalDetails.getUser().getId()));
        }
        return ApiResponse.success(userService.findUserFeedsByCursorId(lastFeedId, limit, principalDetails.getUser().getId()));
    }

    @GetMapping("/comments/feeds")
    public ApiResponse<UserCommentFeedsResponse> findAllFeedsByUserComment(@RequestParam(required = false) Long lastCommentId,
                                                                           @RequestParam(required = false) Integer limit,
                                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        limit = (limit == null) ? 10 : limit;
        if (lastCommentId == null) {
            return ApiResponse.success(userService.findUserCommentFeeds(limit, principalDetails.getUser().getId()));
        }
        return ApiResponse.success(userService.findUserCommentFeedsByCursorId(lastCommentId, limit, principalDetails.getUser().getId()));
    }
}
