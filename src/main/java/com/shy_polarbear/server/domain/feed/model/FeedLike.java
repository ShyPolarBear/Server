package com.shy_polarbear.server.domain.feed.model;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feed_like_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private FeedLike(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }

    public static FeedLike createFeedLike(Feed feed, User user) {
        FeedLike feedLike = FeedLike.builder()
                .user(user)
                .feed(feed)
                .build();
        feed.addLike(feedLike);
        return feedLike;
    }

}
