package com.shy_polarbear.server.domain.user.controller;

import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.*;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.USER_FEED_LIMIT_PARAM_DEFAULT_VALUE;

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
    public ApiResponse<PageResponse<UserFeedResponse>> findAllUserFeeds(@RequestParam(required = false) Long lastFeedId,
                                                           @RequestParam(required = false, defaultValue = USER_FEED_LIMIT_PARAM_DEFAULT_VALUE) Integer limit,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(userService.findUserFeeds(lastFeedId, limit, principalDetails.getUser().getId()));
    }

    @GetMapping("/comments/feeds")
    public ApiResponse<PageResponse<UserCommentFeedResponse>> findAllFeedsByUserComment(@RequestParam(required = false) Long lastCommentId,
                                                                                        @RequestParam(required = false, defaultValue = USER_FEED_LIMIT_PARAM_DEFAULT_VALUE) Integer limit,
                                                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(userService.findAllFeedsByUserComment(lastCommentId, limit, principalDetails.getUser().getId()));
    }
}
