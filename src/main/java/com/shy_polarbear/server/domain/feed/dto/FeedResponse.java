package com.shy_polarbear.server.domain.feed.dto;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FeedResponse {

    private Long feedId;
    private String title;
    private String content;
    private String author;
    private String authorProfileImage;
    private List<String> feedImage = new ArrayList<>();
    private String createdDate;
    private Long commentCount;
    private Long likeCount;
    private Boolean isLike;
    private Boolean isAuthor;

    @Builder
    private FeedResponse(Long feedId, String title, String content, String author, String authorProfileImage, List<String> feedImage, String createdDate, Long commentCount, Long likeCount, Boolean isLike, Boolean isAuthor) {
        this.feedId = feedId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorProfileImage = authorProfileImage;
        this.feedImage = feedImage;
        this.createdDate = createdDate;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.isAuthor = isAuthor;
    }

    public static FeedResponse from(Feed feed, Boolean isLike, Boolean isAuthor) {
        return FeedResponse.builder()
                .feedId(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .author(feed.getAuthor().getNickName())
                .authorProfileImage(feed.getAuthor().getProfileImage())
                .feedImage(feed.getFeedImages())
                .createdDate(feed.getCreatedAt())
                .commentCount(feed.getComments().stream().count())
                .likeCount(feed.getFeedLikes().stream().count())
                .isLike(isLike)
                .isAuthor(isAuthor)
                .build();
    }
}
