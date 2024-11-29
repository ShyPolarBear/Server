package com.shy_polarbear.server.domain.feed.template;

import com.shy_polarbear.server.domain.feed.entity.Feed;
import com.shy_polarbear.server.domain.feed.entity.FeedImage;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.domain.user.template.UserTemplate;

import java.util.List;

public class FeedTemplate {
    public static Feed createDummyFeed() {
        User dummyUser = UserTemplate.createDummyUser();
        List<String> imageUrls = List.of("https://example.com/image.jpg");
        return Feed.createFeed("피드 제목", "피드 내용", FeedImage.createFeedImages(imageUrls), dummyUser);
    }
}
