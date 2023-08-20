package com.shy_polarbear.server.domain.feed.controller;

import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.*;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {
    private final FeedService feedService;

    @PostMapping
    public ApiResponse<CreateFeedResponse> createFeed(@Valid @RequestBody CreateFeedRequest createFeedRequest) {
        return ApiResponse.success(feedService.createFeed(createFeedRequest));
    }

    @GetMapping
    public ApiResponse<?> findAllFeeds(@RequestParam String sort,
                                      @RequestParam(required = false) Long lastFeedId,
                                      @RequestParam(required = false) String limit) {
        return null;
    }

    @GetMapping("/{feedId}")
    public ApiResponse<FeedResponse> findOneFeed(@PathVariable Long feedId) {
        return ApiResponse.success(feedService.findFeed(feedId));
    }

    @PutMapping("/{feedId}")
    public ApiResponse<UpdateFeedResponse> updateFeed(@PathVariable Long feedId,
                                                      @Valid @RequestBody UpdateFeedRequest updateFeedRequest) {
        return ApiResponse.success(feedService.updateFeed(feedId, updateFeedRequest));
    }

    @DeleteMapping("/{feedId}")
    public ApiResponse<DeleteFeedResponse> deleteFeed(@PathVariable Long feedId) {
        return ApiResponse.success(feedService.deleteFeed(feedId));
    }

    @PutMapping("/{feedId}/like")
    public ApiResponse<LikeFeedResponse> switchFeedLike(@PathVariable Long feedId) {
        return ApiResponse.success(feedService.switchFeedLike(feedId));
    }
}
