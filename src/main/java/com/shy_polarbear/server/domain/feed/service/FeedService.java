package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.*;
import com.shy_polarbear.server.domain.feed.exception.FeedException;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedImage;
import com.shy_polarbear.server.domain.feed.model.FeedLike;
import com.shy_polarbear.server.domain.feed.repository.FeedLikeRepository;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.image.service.ImageService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.common.util.LocalDateTimeUtils;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final ImageService imageService;
    private final FeedLikeRepository feedLikeRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CreateFeedResponse createFeed(CreateFeedRequest createFeedRequest, Long userId) {
        User user = userService.getUser(userId);
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

    public FeedResponse findFeed(Long feedId, Long userId) {
        Feed findFeed = findFeedById(feedId);
        return FeedResponse.from(findFeed, findFeed.isLike(userId), findFeed.isAuthor(userId));
    }

    public UpdateFeedResponse updateFeed(Long feedId, UpdateFeedRequest updateFeedRequest, Long userId) {
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(userId, findFeed);
        List<FeedImage> feedImages = getFeedImages(updateFeedRequest.getFeedImages());
        findFeed.update(updateFeedRequest.getTitle(), updateFeedRequest.getContent(), feedImages);
        return new UpdateFeedResponse(feedId);
    }

    public Feed findFeedById(Long feedId) {
        Feed findFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(ExceptionStatus.NOT_FOUND_FEED));
        return findFeed;
    }

    private static void checkFeedAuthor(Long userId, Feed feed) {
        if (!feed.isAuthor(userId)) {
            throw new FeedException(ExceptionStatus.NOT_MY_FEED);
        }
    }

    public DeleteFeedResponse deleteFeed(Long feedId, Long userId) {
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(userId, findFeed);
        feedRepository.delete(findFeed);

        //s3 이미지 삭제
        List<String> feedImageUrls = findFeed.getFeedImageUrls();
        imageService.deleteImages(feedImageUrls);
        return new DeleteFeedResponse(feedId);
    }

    public LikeFeedResponse switchFeedLike(Long feedId, Long userId) {
        User user = userService.getUser(userId);
        Feed feed = findFeedById(feedId);
        if (!feedLikeRepository.existsByUserIdAndFeedId(userId, feedId)) {
            FeedLike feedLike = FeedLike.createFeedLike(feed, user);
            feedLikeRepository.save(feedLike);
            return new LikeFeedResponse("좋아요 처리되었습니다.");
        } else {
            feedLikeRepository.deleteByUserIdAndFeedId(userId, feedId);
            return new LikeFeedResponse("좋아요 취소되었습니다.");
        }
    }

    public PageResponse<FeedCardResponse> findAllFeeds(String sort, Long lastFeedId, int limit, Long userId) {
        //정렬 기준에 따라 피드 엔티티 가져오기
        FeedSort feedSort = FeedSort.toEnum(sort);
        Slice<Feed> feeds = null;
        switch (feedSort) {
            case BEST:
                feeds = findBestFeeds(lastFeedId, limit);
                break;
            case RECENT:
                feeds = findRecentFeeds(lastFeedId, limit);
                break;
            case RECENT_BEST:
                feeds = findRecentBestFeeds(lastFeedId, limit);
                break;
        }

        // 베스트 댓글 or 최신 댓글 가져오기, DTO로 반환
        Slice<FeedCardResponse> feedCardResponses = feeds.map(feed -> FeedCardResponse.of(feed, commentRepository.findBestComment(feed), userId));
        return PageResponse.of(feedCardResponses, feedCardResponses.stream().count());
    }

    private Slice<Feed> findBestFeeds(Long lastFeedId, int limit) {
        return feedRepository.findBestFeeds(generateCursor(lastFeedId), BusinessLogicConstants.BEST_FEED_MIN_LIKE_COUNT, limit);
    }

    private Slice<Feed> findRecentFeeds(Long lastFeedId, int limit) {
        return feedRepository.findRecentFeeds(lastFeedId, limit);
    }

    private Slice<Feed> findRecentBestFeeds(Long lastFeedId, int limit) {
        LocalDateTime earliestLocalDate = LocalDateTime.now()
                .minusDays(BusinessLogicConstants.RECENT_BEST_FEED_DAY_LIMIT);
        String earliestDate = LocalDateTimeUtils.convertToString(earliestLocalDate);
        return feedRepository.findRecentBestFeeds(generateCursor(lastFeedId), earliestDate, limit);
    }

    private String generateCursor(Long lastFeedId){
        if (lastFeedId == null) {
            return null;
        }
        Long feedLikesCount = feedLikeRepository.countFeedLikes(lastFeedId);
        return String.format("%010d", feedLikesCount) + String.format("%019d", lastFeedId);
    }
}
