package com.shy_polarbear.server.domain.user.dto.user.response;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentFeedResponse {

    private Long feedId;
    private String title;
    private String feedImage;
    private String author;
    private String authorProfileImage;
    private Long commentId;

    public static UserCommentFeedResponse from(Feed feed, Long commentId) {
        Long feedId = feed.getId();
        String title = feed.getTitle();
        String author = feed.getAuthor().getNickName();
        String profileImageNullable = feed.getAuthor().getProfileImage();
        String authorProfileImage = (profileImageNullable == null) ? "" : profileImageNullable;
        String feedImage = feed.getFeedImages().size() == 0 ? "" : feed.getFeedImages().get(0).getUrl();
        return new UserCommentFeedResponse(feedId, title, feedImage, author, authorProfileImage, commentId);
    }
}
