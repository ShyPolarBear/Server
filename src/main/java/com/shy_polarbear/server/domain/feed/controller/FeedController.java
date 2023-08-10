package com.shy_polarbear.server.domain.feed.controller;

import com.shy_polarbear.server.domain.feed.dto.*;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {
    private final FeedService feedService;
    private final UserService userService;

    // 피드 작성
    @PostMapping("/")
    public ApiResponse<CreateFeedResponse> createFeed(@RequestBody CreateFeedRequest request) {
        return ApiResponse.success(feedService.createFeed(request, userService));
    }

    // 전체 피드 조회
    @GetMapping("/")
    public ApiResponse<List<FeedsResponse>> getAllFeeds(@RequestParam(required = false) String sort, @RequestParam(required = false) Long lastFeedId, @RequestParam(required = false) String limit) {
        List<FeedsResponse> feedsResponses = feedService.getAllFeeds(sort, lastFeedId, limit);
        return ApiResponse.success(feedsResponses);
    }

    // 피드 상세 조회
    @GetMapping("/{feedId}")
    public ApiResponse<FeedResponse> getFeedById(@PathVariable Long feedId) {
        return feedService.getFeedById(feedId);
    }

    // 피드 수정
    @PutMapping("/{feedId}")
    public ApiResponse<UpdateFeedResponse> updateFeed(@PathVariable Long feedId, @RequestBody UpdateFeedRequest request) {
        UpdateFeedResponse response = feedService.updateFeed(feedId, request);
        return ApiResponse.success(response);
    }

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public ApiResponse<DeleteFeedResponse> deleteFeed(@PathVariable Long feedId) {
        DeleteFeedResponse response = feedService.deleteFeed(feedId);
        return ApiResponse.success(response);
    }

//    @PutMapping("/{feedId}/like")
//    public ApiResponse<?> likeFeed(@RequestBody LikeFeedResponse response, @PathVariable Long feedId) {
//        return ApiResponse.success(feedService.likeFeed(response, feedId));
//    }
//
//    @GetMapping("/{feedId}/report")
//    public ApiResponse<?> reportFeed(@RequestBody ReportFeedResponse response, @PathVariable Long feedId) {
//        return ApiResponse.success(feedService.reportFeed(response, feedId);)
//    }
}
