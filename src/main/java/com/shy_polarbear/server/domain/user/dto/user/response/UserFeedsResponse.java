package com.shy_polarbear.server.domain.user.dto.user.response;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserFeedsResponse {
    private Integer count;
    private Boolean last;
    private List<UserFeedResponse> content;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    private static class UserFeedResponse {
        private Long feedId;
        private String title;
        private String feedImage;

        private static UserFeedResponse from(Feed feed) {
            if (feed.getFeedImages().size() == 0) {
                return new UserFeedResponse(feed.getId(), feed.getTitle(), "");
            }
            return new UserFeedResponse(feed.getId(), feed.getTitle(), feed.getFeedImages().get(0).getUrl());
        }
    }

    @Builder
    public UserFeedsResponse(Boolean last, List<Feed> feedList) {
        this.count = feedList.size();
        this.last = last;
        this.content = feedList.stream()
                .map((feed -> UserFeedResponse.from(feed)))
                .collect(Collectors.toList());
    }
}
