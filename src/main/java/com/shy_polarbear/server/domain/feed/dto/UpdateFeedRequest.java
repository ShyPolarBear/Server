package com.shy_polarbear.server.domain.feed.dto;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UpdateFeedRequest {
    private Long feedId;
    private String title;
    private String content;
    private List<String> feedImage = new ArrayList<>();

    @Builder
    public UpdateFeedRequest(Long feedId, String title, String content, List<String> feedImage) {
        this.feedId = feedId;
        this.title = title;
        this.content = content;
        this.feedImage = feedImage;
    }

    public static UpdateFeedRequest from(Feed feed, Boolean isLike, Boolean isAuthor) {
        return null;
    }
}
