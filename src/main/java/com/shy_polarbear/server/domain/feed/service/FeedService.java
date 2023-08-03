package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.feed.dto.*;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedLike;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    // 피드 작성
    public CreateFeedResponse createFeed(CreateFeedRequest request, UserService userService) {
        Feed newFeed = Feed.createFeed(request.getTitle(), request.getContent(), request.getFeedImages(), userService.getCurruentUser());
        Feed savedFeed = feedRepository.save(newFeed);

        return CreateFeedResponse.builder()
                .feedId(savedFeed.getId())
                .build();
    }

    // 전체 피드 조회(무한 스크롤)
    public List<FeedsResponse> getAllFeeds(String sort, Long lastFeedId, String limit) {
        int pageSize = Integer.parseInt(limit);
        
        if (limit != null) {
            pageSize = Integer.parseInt(limit);
        } else {
            pageSize = 10; // 기본값으로 10을 설정
        }

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sortCriteria = Sort.by(direction, "id");

        // 마지막 피드 ID가 주어진 경우 해당 피드 ID 이후의 피드를 가져옴
        if (lastFeedId != null) {
            Pageable pageable = PageRequest.of(0, pageSize, sortCriteria);
            List<Feed> feeds = feedRepository.findAllByIdGreaterThanOrderByIdDesc(lastFeedId, pageable);
            return mapFeedsToFeedsResponse(feeds);
        }

        // 마지막 피드 ID가 주어지지 않은 경우 처음부터 pageSize 개수의 피드를 가져옴
        Pageable pageable = PageRequest.of(0, pageSize, sortCriteria);
        Page<Feed> feedPage = feedRepository.findAll(pageable);
        List<Feed> feeds = feedPage.getContent();
        return mapFeedsToFeedsResponse(feeds);
    }

    private List<FeedsResponse> mapFeedsToFeedsResponse(List<Feed> feeds) {
        return feeds.stream()
                .map(feed -> FeedsResponse.from(feed, false, false))
                .collect(Collectors.toList());
    }

    // 피드 상세 조회
    public ApiResponse<FeedResponse> getFeedById(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElse(null);
        if (feed == null) {
            return ApiResponse.fail(2001, "존재하지 않는 피드입니다.");
        }
        FeedResponse feedResponse = FeedResponse.from(feed, false, false);
        return ApiResponse.success(feedResponse);
    }

    // 피드 수정
    public UpdateFeedResponse updateFeed(Long feedId, UpdateFeedRequest request) {
        // 1. 피드 ID로 해당 피드를 데이터베이스에서 조회한다.
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if (optionalFeed.isEmpty()) {
            // 2. 피드가 존재하지 않는 경우, 실패 응답을 생성하여 반환한다.
            return UpdateFeedResponse.builder()
                    .feedId(null)
                    .code(2001L) // 2001은 존재하지 않는 피드 에러 코드를 의미
                    .message("존재하지 않는 피드입니다.")
                    .build();
        }

        // 3. 요청으로 받은 데이터로 피드를 수정한다.
        Feed feedToUpdate = optionalFeed.get();

        // 피드 엔티티 클래스에 update 메서드를 추가하여 필드 값을 변경한다.
        feedToUpdate.update(request.getTitle(), request.getContent(), request.getFeedImage());
        
        // 4. 수정된 피드를 데이터베이스에 저장한다.
        Feed updateFeed = feedRepository.save(feedToUpdate);
        
        // 5. 성공 응답을 생성하여 반환한다.
        return UpdateFeedResponse.builder()
                .feedId(updateFeed.getId())
                .code(0L) // 0은 성공 코드를 의미
                .message("")
                .build();
    }

    // 피드 삭제
    public DeleteFeedResponse deleteFeed(Long feedId) {
        // 1. 피드 ID로 해당 피드를 데이터베이스에서 조회한다.
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if (optionalFeed.isEmpty()) {
            // 2. 피드가 존재하지 않는 경우, 실패 응답을 생성하여 반환한다.
            DeleteFeedResponse response = DeleteFeedResponse.builder()
                    .feedId(null)
//                    .code(2001L) // 2001은 존재하지 않는 피드 에러 코드를 의미
//                    .message("존재하지 않는 피드입니다.")
                    .build();
            return response;
        }

        // 3. 피드를 데이터베이스에서 삭제한다.
        feedRepository.deleteById(feedId);

        // 4. 성공 응답을 생성하여 반환한다.
        DeleteFeedResponse response = DeleteFeedResponse.builder()
                .feedId(feedId)
//                .code(0L) // 0은 성공 코드를 의미
//                .message("")
                .build();
        return response;
    }

    // 피드 좋아요
//    public LikeFeedResponse likeFeed(Long feedId, User user) {
//        Feed feed = feedRepository.findById(feedId).orElse(null);
//        if (feed == null) {
//            return null;
//        }
//
//        boolean isLiked = feed.isLikedByUser(user);
//        if (!isLiked) {
//            FeedLike feedLike = FeedLike.createFeedLike(feed, user);
//            feed.addLike(feedLike);
//        }
//
//        return LikeFeedResponse.builder()
//                .feedLikeId(feed.getLikeId(user))
//                .build();
//    }

    // 피드 신고
//    public void reportFeed(ReportFeedResponse response, Long feedId, User user) {
//        Feed feed = feedRepository.findById(feedId).orElse(null);
//        if (feed == null) {
//            throw  new IllegalArgumentException("해당 피드를 찾을 수 없습니다.");
//        }
//
//        // 본인 피드를 신고할 경우, 에러 처리
//        if (feed.getAuthor().getId().equals(user.getId())) {
//            throw new IllegalStateException("본인의 피드를 신고할 수 없습니다.");
//        }
//
//        // 이미 신고한 피드인지 확인
//        if (feed.isReportedByUser(user)) {
//            throw new IllegalStateException("이미 신고한 피드입니다.");
//        }
//
//        // 피드 신고 객체 생성 및 저장
//        FeedReport feedReport = FeedReport.createFeedReport(feed, user);
//        feed.addReport(feedReport);
//    }
}
