package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.CreateFeedResponse;
import com.shy_polarbear.server.domain.feed.dto.response.FeedResponse;
import com.shy_polarbear.server.domain.feed.dto.response.LikeFeedResponse;
import com.shy_polarbear.server.domain.feed.exception.FeedException;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedImage;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.model.ProviderType;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("FeedService 클래스")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class FeedServiceTest {
    private Feed saveFeed;
    private User user;
    private List<String> imageUrls = new ArrayList<>();
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private FeedService feedService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Mock
    User anotherUser;

    @BeforeEach
    void setUp() {
        user = User.createUser("노을", "chi6465618@naver.com", null, "01093926465", UserRole.ROLE_USR, "1", ProviderType.KAKAO, passwordEncoder);
        userRepository.save(user);

        imageUrls.add("테스트 사진");
        Feed feed = Feed.createFeed("피드 제목", "피드 내용", FeedImage.createFeedImages(imageUrls), user);
        saveFeed = feedRepository.save(feed);
    }

    @DisplayName("createFeed() 메서드는 CreateFeedResponse에 feedId를 담아 리턴한다.")
    @Test
    void createFeed() {
        CreateFeedRequest createFeedRequest = new CreateFeedRequest("111", "111", imageUrls);
        CreateFeedResponse createFeedResponse = feedService.createFeed(createFeedRequest, user.getId());
        assertThat(createFeedResponse.getFeedId()).isNotNull();
    }

    @DisplayName("findFeed()메서드는 피드가 존재하지 않는다면 FeedException을 던진다.")
    @Test
    void findFeed_exception() {
        assertThatThrownBy( () -> feedService.findFeed(-1L, user.getId()))
                .isInstanceOf(FeedException.class)
                .hasMessage(ExceptionStatus.NOT_FOUND_FEED.getMessage());
    }

    @DisplayName("findFeed()메서드는 피드가 존재하면 FeedResponse를 리턴한다.")
    @Test
    void findFeed() {
        FeedResponse feedResponse = feedService.findFeed(saveFeed.getId(), user.getId());
        assertThat(feedResponse.getFeedId()).isEqualTo(saveFeed.getId());
    }

    //수정 테스트 실패함
    @DisplayName("updateFeed() 메서드는 피드를 수정한다.")
    @Test
    void updateFeed() {
        UpdateFeedRequest updateFeedRequest = new UpdateFeedRequest("수정 제목", "수정 내용", null);
        feedService.updateFeed(saveFeed.getId(), updateFeedRequest, user.getId());
        Feed feed = feedRepository.findById(saveFeed.getId()).get();
        assertThat(feed.getTitle()).isEqualTo("수정 제목");
    }

    @DisplayName("updateFeed() 메서드는 유저가 피드 작성자가 아니라면 FeedException을 던진다.")
    @Test
    void updateFeed_exception() {
        UpdateFeedRequest updateFeedRequest = new UpdateFeedRequest("수정 제목", "수정 내용", null);
        assertThatThrownBy(() -> feedService.updateFeed(saveFeed.getId(), updateFeedRequest, anotherUser.getId()))
                .isInstanceOf(FeedException.class)
                .hasMessage(ExceptionStatus.NOT_MY_FEED.getMessage());
    }

    @DisplayName("switchFeedLike()는 좋아요 취소 상태에서 좋아요를 실행한다.")
    @Test
    void switchFeedLike_like() {
        LikeFeedResponse likeFeedResponse = feedService.switchFeedLike(saveFeed.getId(), user.getId());
        assertThat(likeFeedResponse.getResult()).isEqualTo("좋아요 처리되었습니다.");
    }

    @DisplayName("switchFeedLike()는 좋아요 상태에서 좋아요 취소를 실행한다.")
    @Test
    void switchFeedLike_cancel() {
        feedService.switchFeedLike(saveFeed.getId(), user.getId());
        LikeFeedResponse likeFeedResponse = feedService.switchFeedLike(saveFeed.getId(), user.getId());
        assertThat(likeFeedResponse.getResult()).isEqualTo("좋아요 취소되었습니다.");
    }
}