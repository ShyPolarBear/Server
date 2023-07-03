package com.shy_polarbear.server.domain.feed.entity;

import com.shy_polarbear.server.domain.user.entity.User;
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
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;
    private String title;
    private String content;
    @OneToMany(mappedBy = "feed")
    private final List<FeedLike> feedLikes = new ArrayList<>();
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "feed_images", joinColumns = @JoinColumn(name = "feed_id"))
    @Column(name = "feed_image")
    private List<String> feedImages = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Builder
    private Feed(String title, String content, List<String> feedImages, User author) {
        this.title = title;
        this.content = content;
        this.feedImages = feedImages;
        this.author = author;
    }

    public static Feed createFeed(String title, String content, List<String> feedImages, User author) {
        return Feed.builder()
                .title(title)
                .content(content)
                .feedImages(feedImages)
                .author(author)
                .build();
    }

    public void addLike(FeedLike feedLike) {
        this.feedLikes.add(feedLike);
    }
}
