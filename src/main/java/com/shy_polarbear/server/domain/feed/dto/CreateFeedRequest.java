package com.shy_polarbear.server.domain.feed.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateFeedRequest {

    private String title;
    private String content;
    private List<String> feedImages = new ArrayList<>();

    @Builder
    private CreateFeedRequest(String title, String content, List<String> feedImages) {
        this.title = title;
        this.content = content;
        this.feedImages = feedImages;
    }
}
