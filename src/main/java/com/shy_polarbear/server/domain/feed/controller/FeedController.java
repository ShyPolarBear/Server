package com.shy_polarbear.server.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    @PostMapping("/")
    public void createFeed() {
    }
    @GetMapping("/")
    public void getAllFeeds(@RequestParam String sort,
                            @RequestParam(required = false) String lastFeedId,
                            @RequestParam(required = false) String limit) {
    }

    @GetMapping("/{feedId}")
    public void getFeed(@PathVariable Long feedId) {
    }

    @PutMapping("/{feedId}")
    public void updateFeed(@PathVariable Long feedId) {
    }

    @DeleteMapping("/{feedId}")
    public void deleteFeed(@PathVariable Long feedId) {
    }

    @PutMapping("/{feedId}/like")
    public void addLike(@PathVariable Long feedId) {
    }

    @GetMapping("/{feedId}/report")
    public void reportFeed(@PathVariable Long feedId) {
    }
}
