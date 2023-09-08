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
import com.shy_polarbear.server.domain.images.service.ImageService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final ImageService imageService;
    private final FeedLikeRepository feedLikeRepository;
    private final CommentRepository commentRepository;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public PageResponse<FeedCardResponse> findAllFeeds(String sort, Long lastFeedId, int limit, User user) {
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
        Slice<FeedCardResponse> feedCardResponses = feeds.map(feed -> FeedCardResponse.of(feed, commentRepository.findBestComment(feed), user));
        return PageResponse.of(feedCardResponses, feedCardResponses.stream().count());
    }

    private Slice<Feed> findBestFeeds(Long lastFeedId, int limit) {
        return feedRepository.findBestFeeds(generateCursor(lastFeedId), BusinessLogicConstants.BEST_FEED_MIN_LIKE_COUNT, limit);
    }

    private Slice<Feed> findRecentFeeds(Long lastFeedId, int limit) {
        return feedRepository.findRecentFeeds(lastFeedId, limit);
    }

    private Slice<Feed> findRecentBestFeeds(Long lastFeedId, int limit) {
        String earliestDate = LocalDateTime.now()
                .minusDays(BusinessLogicConstants.RECENT_BEST_FEED_DAY_LIMIT)
                .format(dateTimeFormatter);
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
