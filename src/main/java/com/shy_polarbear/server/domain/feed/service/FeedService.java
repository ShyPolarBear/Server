package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.feed.dto.request.CreateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.request.UpdateFeedRequest;
import com.shy_polarbear.server.domain.feed.dto.response.CreateFeedResponse;
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

    public FeedResponse findFeedById(Long feedId) {
        Feed findFeed = findFeed(feedId);
        return FeedResponse.from(findFeed);
    }

    public UpdateFeedResponse updateFeed(Long feedId, UpdateFeedRequest updateFeedRequest) {
        User user = userService.getCurruentUser();
        Feed findFeed = findFeed(feedId);
        checkFeedAuthor(user, findFeed);
        findFeed.update(updateFeedRequest.getTitle(), updateFeedRequest.getContent(), updateFeedRequest.getFeedImages());
        return new UpdateFeedResponse(findFeed.getId());
    }

    private Feed findFeed(Long feedId) {
        Feed findFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedException(ExceptionStatus.NOT_FOUND_FEED));
        return findFeed;
    }

    private static void checkFeedAuthor(User user, Feed feed) {
        if (!feed.isAuthor(user)) {
            throw new FeedException(ExceptionStatus.NOT_MY_FEED);
        }
    }
}
