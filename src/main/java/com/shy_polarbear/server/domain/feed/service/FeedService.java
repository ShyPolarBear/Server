package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.*;
import com.shy_polarbear.server.domain.feed.exception.FeedException;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedImage;
import com.shy_polarbear.server.domain.feed.model.FeedLike;
import com.shy_polarbear.server.domain.feed.repository.FeedLikeRepository;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.images.service.ImageService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final FeedLikeRepository feedLikeRepository;

    public CreateFeedResponse createFeed(CreateFeedRequest createFeedRequest) {
        User user = userService.getCurruentUser();
        List<FeedImage> feedImages = FeedImage.createFeedImages(createFeedRequest.getFeedImages());
        Feed feed = Feed.createFeed(createFeedRequest.getTitle(), createFeedRequest.getContent(), feedImages, user);
        feedRepository.save(feed);
        return new CreateFeedResponse(feed.getId());
    }

    public FeedResponse findFeed(Long feedId) {
        User user = userService.getCurruentUser();
        Feed findFeed = findFeedById(feedId);
        return FeedResponse.from(findFeed, findFeed.isLike(user), findFeed.isAuthor(user));
    }

    public UpdateFeedResponse updateFeed(Long feedId, UpdateFeedRequest updateFeedRequest) {
        User user = userService.getCurruentUser();
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(user, findFeed);
        List<FeedImage> feedImages = FeedImage.createFeedImages(updateFeedRequest.getFeedImages());
        findFeed.update(updateFeedRequest.getTitle(), updateFeedRequest.getContent(), feedImages);
        return new UpdateFeedResponse(feedId);
    }

    private Feed findFeedById(Long feedId) {
        Feed findFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(ExceptionStatus.NOT_FOUND_FEED));
        return findFeed;
    }

    private static void checkFeedAuthor(User user, Feed feed) {
        if (!feed.isAuthor(user)) {
            throw new FeedException(ExceptionStatus.NOT_MY_FEED);
        }
    }

    public DeleteFeedResponse deleteFeed(Long feedId) {
        User user = userService.getCurruentUser();
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(user, findFeed);
        feedRepository.delete(findFeed);

        //s3 이미지 삭제
        List<String> feedImageUrls = findFeed.getFeedImageUrls();
        imageService.deleteImages(feedImageUrls);
        return new DeleteFeedResponse(feedId);
    }

    public LikeFeedResponse switchFeedLike(Long feedId) {
        User user = userService.getCurruentUser();
        Feed feed = findFeedById(feedId);
        if (!feedLikeRepository.existsByUserAndFeed(user, feed)) {
            FeedLike feedLike = FeedLike.createFeedLike(feed, user);
            feedLikeRepository.save(feedLike);
            return new LikeFeedResponse("좋아요 처리되었습니다.");
        } else {
            feedLikeRepository.deleteByUserAndFeed(user, feed);
            return new LikeFeedResponse("좋아요 취소되었습니다.");
        }
    }
}
