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
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedService {

    private final FeedRepository feedRepository;
    private final ImageService imageService;
    private final FeedLikeRepository feedLikeRepository;

    public CreateFeedResponse createFeed(CreateFeedRequest createFeedRequest, User user) {
        List<String> imageUrls = createFeedRequest.getFeedImages();
        List<FeedImage> feedImages = getFeedImages(imageUrls);
        Feed feed = Feed.createFeed(createFeedRequest.getTitle(), createFeedRequest.getContent(), feedImages, user);
        feedRepository.save(feed);
        return new CreateFeedResponse(feed.getId());
    }

    private static List<FeedImage> getFeedImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.size() == 0) {
            return null;
        } else {
            return FeedImage.createFeedImages(imageUrls);
        }
    }

    public FeedResponse findFeed(Long feedId, User user) {
        Feed findFeed = findFeedById(feedId);
        return FeedResponse.from(findFeed, findFeed.isLike(user), findFeed.isAuthor(user));
    }

    public UpdateFeedResponse updateFeed(Long feedId, UpdateFeedRequest updateFeedRequest, User user) {
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(user, findFeed);
        List<FeedImage> feedImages = getFeedImages(updateFeedRequest.getFeedImages());
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

    public DeleteFeedResponse deleteFeed(Long feedId, User user) {
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(user, findFeed);
        feedRepository.delete(findFeed);

        //s3 이미지 삭제
        List<String> feedImageUrls = findFeed.getFeedImageUrls();
        imageService.deleteImages(feedImageUrls);
        return new DeleteFeedResponse(feedId);
    }

    public LikeFeedResponse switchFeedLike(Long feedId, User user) {
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
