package com.shy_polarbear.server.global.common.dummy;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedImage;
import com.shy_polarbear.server.domain.feed.model.FeedLike;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.common.constants.ProfileConstants;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.shy_polarbear.server.global.common.dummy.LoginUserInitializer.LOGIN_USER_PROVIDER_ID;

@Component("FeedInitializer")
@DependsOn("LoginUserInitializer")
@RequiredArgsConstructor
@Slf4j
@Transactional
@Profile({ProfileConstants.LOCAL, ProfileConstants.DEV})
public class FeedInitializer {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        createDummyFeed();
    }

    private void createDummyFeed() {
        User user = userRepository.findByProviderId(LOGIN_USER_PROVIDER_ID)
                .orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
        if (feedRepository.count() == 0) {
            log.info("더미 피드 20개를 생성합니다.");
            for (int i = 0; i < 10; i++) {
                List<String> feedUrls = new ArrayList<>();
                feedUrls.add("테스트 사진");
                List<FeedImage> feedImages = FeedImage.createFeedImages(feedUrls);
                Feed feed = Feed.createFeed("제목" + i, "", feedImages, user);
                for (int j = 0; j < i; j++) {
                    FeedLike.createFeedLike(feed, user);
                }
                feedRepository.save(feed);
            }
            for (int i = 0; i < 10; i++) {
                List<String> feedUrls = new ArrayList<>();
                feedUrls.add("테스트 사진");
                List<FeedImage> feedImages = FeedImage.createFeedImages(feedUrls);
                Feed feed = Feed.createFeed("제목" + (i + 10), "", feedImages, user);
                for (int j = i; j > 0; j--) {
                    FeedLike.createFeedLike(feed, user);
                }
                feedRepository.save(feed);
            }
        }
    }
}