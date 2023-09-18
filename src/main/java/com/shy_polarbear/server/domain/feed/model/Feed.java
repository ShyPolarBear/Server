package com.shy_polarbear.server.domain.feed.model;


import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
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
    private Feed(String title, String content, User author, List<FeedImage> feedImages) {
        this.title = title;
        this.content = content;
        this.author = author;
        if (!Objects.isNull(feedImages)) {
            this.feedImages.addAll(feedImages);
        }
    }

    public static Feed createFeed(String title, String content, List<FeedImage> feedImages, User author) {
        Feed feed = Feed.builder()
                .title(title)
                .content(content)
                .author(author)
                .feedImages(feedImages)
                .build();
        assignFeedToFeedImages(feed);
        return feed;
    }

    private static void assignFeedToFeedImages(Feed feed) {
        if (!feed.feedImages.isEmpty()) {
            feed.feedImages.stream()
                    .forEach(feedImage -> feedImage.assignFeed(feed));
        }
    }

    public void addLike(FeedLike feedLike) {
        this.feedLikes.add(feedLike);
    }

    public void update(String title, String content, List<FeedImage> feedImages) {
        this.title = title;
        this.content = content;
        this.feedImages.clear();
        if (feedImages != null) {
            this.feedImages.addAll(feedImages);
            assignFeedToFeedImages(this);
        }
    }

    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }

    public boolean isLike(User user) {
        return feedLikes.stream()
                .anyMatch(feedLike -> feedLike.isAuthor(user));
    }

    public List<String> getFeedImageUrls() {
        return feedImages.stream()
                .map(feedImage -> feedImage.getUrl())
                .toList();
    }
}
