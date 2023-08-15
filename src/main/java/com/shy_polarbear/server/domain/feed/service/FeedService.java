package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.CreateFeedResponse;
import com.shy_polarbear.server.domain.feed.dto.response.DeleteFeedResponse;
import com.shy_polarbear.server.domain.feed.dto.response.FeedResponse;
import com.shy_polarbear.server.domain.feed.dto.response.UpdateFeedResponse;
import com.shy_polarbear.server.domain.feed.exception.FeedException;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserService userService;

    public CreateFeedResponse createFeed(CreateFeedRequest createFeedRequest) {
        User user = userService.getCurruentUser();
        Feed feed = Feed.createFeed(createFeedRequest.getTitle(), createFeedRequest.getContent(), createFeedRequest.getFeedImages(), user);
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
        findFeed.update(updateFeedRequest.getTitle(), updateFeedRequest.getContent(), updateFeedRequest.getFeedImages());
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
        //피드가 삭제되면, 피드 좋아요도 삭제된다.
        //피드가 삭제되면, 피드에 달린 댓글들도 삭제된다.
        //피드가 삭제되면, 피드 이미지들도 삭제된다.
        User user = userService.getCurruentUser();
        Feed findFeed = findFeedById(feedId);
        checkFeedAuthor(user, findFeed);
        feedRepository.delete(findFeed);
        return new DeleteFeedResponse(feedId);
    }
}
