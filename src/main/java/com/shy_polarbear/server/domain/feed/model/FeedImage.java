package com.shy_polarbear.server.domain.feed.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_image_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;
    @Column(length = 2000)
    private String url;

    public FeedImage(String url) {
        this.url = url;
    }

    public static List<FeedImage> createFeedImages(List<String> urls) {
        return urls.stream()
                .map(url -> new FeedImage(url))
                .toList();
    }

    void assignFeed(Feed feed) {
        this.feed = feed;
    }
}
