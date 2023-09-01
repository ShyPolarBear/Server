package com.shy_polarbear.server.domain.feed.dto.response;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FeedCardResponse {
    private Long feedId;
    private String title;
    private String content;
    private Integer likeCount;
    private List<String> feedImages;
    private String author;
    private String authorProfileImage;
    private String createdDate;
    private Boolean isLike;
    private Boolean isAuthor;
    private Integer commentCount;
    private FeedCommentResponse comment;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FeedCommentResponse {
        private Long commentId;
        private String author;
        private String authorProfileImage;
        private String content;
        private Integer likeCount;
        private Boolean isAuthor;
        private Boolean isLike;
        private String createdDate;
    }

    public static FeedCardResponse of(Feed feed, Boolean isFeedAuthor, Boolean isFeedLike,
                                      int commentCount, Comment comment, Boolean isCommentAuthor, Boolean isCommentLike) {
        User commentAuthor = comment.getAuthor();
        FeedCommentResponse feedCommentResponse = FeedCommentResponse.builder()
                .commentId(comment.getId())
                .author(commentAuthor.getNickName())
                .authorProfileImage((commentAuthor.getProfileImage() == null) ? "" : commentAuthor.getProfileImage())
                .content(comment.getContent())
                .likeCount(comment.getCommentLikes().size())
                .isAuthor(isCommentAuthor)
                .isLike(isCommentLike)
                .createdDate(comment.getCreatedDate())
                .build();

        User feedAuthor = feed.getAuthor();
        FeedCardResponse feedCardResponse = FeedCardResponse.builder()
                .feedId(feed.getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .likeCount(feed.getFeedLikes().size())
                .feedImages(feed.getFeedImageUrls())
                .author(feedAuthor.getNickName())
                .authorProfileImage((feedAuthor.getProfileImage() == null) ? "" : feedAuthor.getProfileImage())
                .createdDate(feed.getCreatedDate())
                .isLike(isFeedLike)
                .isAuthor(isFeedAuthor)
                .commentCount(commentCount)
                .comment(feedCommentResponse)
                .build();
        return feedCardResponse;
    }
}
