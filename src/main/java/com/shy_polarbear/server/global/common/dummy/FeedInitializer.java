package com.shy_polarbear.server.global.common.dummy;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedInitializer {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        createDummyFeed();
    }

    @Transactional
    void createDummyFeed() {
        User user = userRepository.findByProviderId("0000")
                .orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
        if (feedRepository.count() == 0) {
            log.info("더미 피드 10개를 생성합니다.");
            for (int i = 0; i < 10; i++) {
                List<String> feedImages = new ArrayList<>();
                feedImages.add("테스트 사진");
                Feed feed = Feed.createFeed("제목" + i, null, feedImages, user);
                feedRepository.save(feed);
            }
        }
    }
}
