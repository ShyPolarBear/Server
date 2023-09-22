package com.shy_polarbear.server.domain.user.dto.user.response;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserFeedResponse {
    private Long feedId;
    private String title;
    private String feedImage;

    public static UserFeedResponse from(Feed feed) {
        String feedImage = feed.getFeedImages().size() == 0 ? "" : feed.getFeedImages().get(0).getUrl();
        return new UserFeedResponse(feed.getId(), feed.getTitle(), feedImage);
    }
}
