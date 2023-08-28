package com.shy_polarbear.server.domain.feed.model;


import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<FeedLike> feedLikes = new ArrayList<>();
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImage> feedImages = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Feed(String title, String content, List<FeedImage> feedImages, User author) {
        this.title = title;
        this.content = content;
        this.feedImages.addAll(feedImages);
        this.author = author;
    }

    public static Feed createFeed(String title, String content, List<FeedImage> feedImages, User author) {
        if (feedImages == null) {
            return Feed.builder()
                    .title(title)
                    .content(content)
                    .author(author)
                    .build();
        } else {
            Feed feed = Feed.builder()
                    .title(title)
                    .content(content)
                    .feedImages(feedImages)
                    .author(author)
                    .build();
            assignFeedToFeedImages(feedImages, feed);
            return feed;
        }
    }

    private static void assignFeedToFeedImages(List<FeedImage> feedImages, Feed feed) {
        feedImages.stream()
                .forEach(feedImage -> feedImage.assignFeed(feed));
    }

    public void addLike(FeedLike feedLike) {
        this.feedLikes.add(feedLike);
    }

    public void update(String title, String content, List<FeedImage> feedImages) {
        this.title = title;
        this.content = content;
        this.feedImages.clear();
        if (feedImages != null) {
            assignFeedToFeedImages(feedImages, this);
            this.feedImages.addAll(feedImages);
        }
    }

    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }

    public boolean isLike(User user) {
        return feedLikes.stream()
                .anyMatch(feedLike -> feedLike.isUser(user));
    }

    public List<String> getFeedImageUrls() {
        return feedImages.stream()
                .map(feedImage -> feedImage.getUrl())
                .toList();
    }
}
