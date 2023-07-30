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
    private Boolean isLast;
    private List<UserFeedResponse> myFeedList;

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
            String authorProfileImage = feed.getAuthor().getProfileImage();
            if (feed.getFeedImages().size() == 0) {
                return new UserFeedResponse(feedId, title, null, author, authorProfileImage);
            }
            return new UserFeedResponse(feedId, title, feed.getFeedImages().get(0), author, authorProfileImage);
        }
    }

    @Builder
    public UserCommentFeedsResponse(Boolean isLast, List<Feed> feedList) {
        this.count = feedList.size();
        this.isLast = isLast;
        this.myFeedList = feedList.stream()
                .map((feed -> UserFeedResponse.from(feed)))
                .collect(Collectors.toList());
    }
}
