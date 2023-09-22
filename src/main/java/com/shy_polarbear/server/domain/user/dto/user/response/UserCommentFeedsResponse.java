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
public class UserCommentFeedsResponse {
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
        private String author;
        private String authorProfileImage;

        private static UserFeedResponse from(Feed feed) {
            Long feedId = feed.getId();
            String title = feed.getTitle();
            String author = feed.getAuthor().getNickName();
            String profileImageNullable = feed.getAuthor().getProfileImage();
            String authorProfileImage = (profileImageNullable == null) ? "" : profileImageNullable;
            if (feed.getFeedImages().size() == 0) {
                return new UserFeedResponse(feedId, title, null, author, authorProfileImage);
            }
            return new UserFeedResponse(feedId, title, feed.getFeedImages().get(0).getUrl(), author, authorProfileImage);
        }
    }

    @Builder
    public UserCommentFeedsResponse(Boolean last, List<Feed> feedList) {
        this.count = feedList.size();
        this.last = last;
        this.content = feedList.stream()
                .map((feed -> UserFeedResponse.from(feed)))
                .collect(Collectors.toList());
    }
}
