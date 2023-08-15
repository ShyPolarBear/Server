package com.shy_polarbear.server.domain.feed.dto.response;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedResponse {
    private Long feedId;
    private String title;
    private String content;
    private String author;
    private String authorProfileImage;
    private List<String> feedImage;
    private String createdDate;
    private Integer commentCount;
    private Integer likeCount;
    private Boolean isLike;
    private Boolean isAuthor;

    public static FeedResponse from(Feed feed) {
//        return FeedResponse.builder()
//                .feedId(feed.getId())
//                .title(feed.getTitle())
//                .content(feed.getContent())
//                .author(feed.getAuthor().getNickName())
//                .authorProfileImage(feed.getAuthor().getProfileImage())
//                .feedImage(feed.getFeedImages())
//                .createdDate(feed.getCreatedAt())
//                .commentCount(feed.getComments().size())
//                .likeCount(feed.getFeedLikes().size())
//                .isLike()
//                .isAuthor()
//                .build();
        return null;
    }
}
