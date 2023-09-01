package com.shy_polarbear.server.domain.feed.controller;

import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.*;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {
    private final FeedService feedService;

    @PostMapping
    public ApiResponse<CreateFeedResponse> createFeed(@Valid @RequestBody CreateFeedRequest createFeedRequest,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(feedService.createFeed(createFeedRequest, principalDetails.getUser()));
    }

    @GetMapping
    public ApiResponse<?> findAllFeeds(@RequestParam String sort,
                                      @RequestParam(required = false) Long lastFeedId,
                                      @RequestParam(required = false, defaultValue = BusinessLogicConstants.FEED_LIMIT_PARAM_DEFAULT_VALUE) int limit,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(feedService.findAllFeeds(sort, lastFeedId, limit, principalDetails.getUser()));
    }

    @GetMapping("/{feedId}")
    public ApiResponse<FeedResponse> findOneFeed(@PathVariable Long feedId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(feedService.findFeed(feedId, principalDetails.getUser()));
    }

    @PutMapping("/{feedId}")
    public ApiResponse<UpdateFeedResponse> updateFeed(@PathVariable Long feedId,
                                                      @Valid @RequestBody UpdateFeedRequest updateFeedRequest,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(feedService.updateFeed(feedId, updateFeedRequest, principalDetails.getUser()));
    }

    @DeleteMapping("/{feedId}")
    public ApiResponse<DeleteFeedResponse> deleteFeed(@PathVariable Long feedId,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(feedService.deleteFeed(feedId, principalDetails.getUser()));
    }

    @PutMapping("/{feedId}/like")
    public ApiResponse<LikeFeedResponse> switchFeedLike(@PathVariable Long feedId,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.success(feedService.switchFeedLike(feedId, principalDetails.getUser()));
    }
}
