package com.shy_polarbear.server.domain.feed.dto;

import com.shy_polarbear.server.domain.feed.model.Feed;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FeedsResponse {
    private Long code;
    private Long feedCount;
    private Boolean isLast;
    private Long feedId;
    private String title;
    private String content;
    private Long likeCount;
    private List<String> feedImage = new ArrayList<>();
    private String author;
    private List<String> authorProfileImage = new ArrayList<>();
    private String createdDate;
    private Boolean isLike;
    private Boolean isAuthor;
    private Long commentCount;
    private Long commentcommentId;
    private String commentauthor;
    private List<String> commentauthorProfileImage = new ArrayList<>();
    private String commentcontent;
    private Long commentlikeCount;
    private Boolean commentisAuthor;
    private Boolean commentisLike;
    private String commentcreatedDate;
    private String message;

    @Builder
    private FeedsResponse(Long code, Long feedCount, Boolean isLast, Long feedId, String title, String content, Long likeCount,
                          List<String> feedImage, String author, List<String> authorProfileImage, String createdDate,
                          Boolean isLike, Boolean isAuthor, Long commentCount, Long commentcommentId, String commentauthor,
                          List<String> commentauthorProfileImage, String commentcontent, Long commentlikeCount,
                          Boolean commentisAuthor, Boolean commentisLike, String commentcreatedDate, String message) {

        this.code = code;
        this.feedCount = feedCount;
        this.isLast = isLast;
        this.feedId = feedId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.feedImage = feedImage;
        this.author = author;
        this.authorProfileImage = authorProfileImage;
        this.createdDate = createdDate;
        this.isLike = isLike;
        this.isAuthor = isAuthor;
        this.commentCount = commentCount;
        this.commentcommentId = commentcommentId;
        this.commentauthor = commentauthor;
        this.commentauthorProfileImage = commentauthorProfileImage;
        this.commentcontent = commentcontent;
        this.commentlikeCount = commentlikeCount;
        this.commentisAuthor = commentisAuthor;
        this.commentisLike = commentisLike;
        this.commentcreatedDate = commentcreatedDate;
        this.message = message;
    }

    public static FeedsResponse from(Feed feed, Boolean isLike, Boolean isAuthor) {
        return null;
    }
}
