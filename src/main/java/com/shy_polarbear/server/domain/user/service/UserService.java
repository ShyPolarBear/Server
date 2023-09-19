package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.*;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    //닉네임 중복 검증
    public DuplicateNicknameResponse checkDuplicateNickName(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new DuplicateNicknameException(ExceptionStatus.NICKNAME_DUPLICATION, new DuplicateNicknameResponse(false));
        }
        return new DuplicateNicknameResponse(true);
    }

    //이미 가입한 회원 검증
    public void checkDuplicateUser(String providerId) {
        if (userRepository.existsByProviderId(providerId)) {
            throw new UserException(ExceptionStatus.USER_ALREADY_EXISTS);
        }
    }

    public Long saveUser(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public UserInfoResponse findUserInfo(Long userId) {
        User user = getUser(userId);
        return UserInfoResponse.from(user);
    }

    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest userInfoRequest, Long userId) {
        User user = getUser(userId);
        if (!user.isSameNickName(userInfoRequest.getNickName())) {
            checkDuplicateNickName(userInfoRequest.getNickName());
        }
        user.updateInfo(userInfoRequest.getNickName(), userInfoRequest.getProfileImage(), userInfoRequest.getEmail(), userInfoRequest.getPhoneNumber());
        return UpdateUserInfoResponse.from(user);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
    }

    public UserFeedsResponse findUserFeedsByCursorId(Long lastFeedId, Integer limit, Long userId) {
        Slice<Feed> findFeedList = feedRepository.findByIdLessThanAndAuthorIdOrderByIdDesc(lastFeedId, userId, PageRequest.of(0, limit));
        return getUserFeedsResponse(findFeedList);
    }

    public UserFeedsResponse findUserFeeds(Integer limit, Long userId) {
        Slice<Feed> findFeedList = feedRepository.findByAuthorIdOrderByIdDesc(userId, PageRequest.of(0, limit));
        return getUserFeedsResponse(findFeedList);
    }

    public UserCommentFeedsResponse findUserCommentFeedsByCursorId(Long lastCommentId, Integer limit, Long userId) {
        Slice<Feed> findFeedList = feedRepository.findByCommentsAuthorIdAndCommentsIdLessThanOrderByCommentsIdDesc(userId, lastCommentId, PageRequest.of(0, limit));
        return getUserCommentFeedsResponse(findFeedList);
    }

    public UserCommentFeedsResponse findUserCommentFeeds(Integer limit, Long userId) {
        Slice<Feed> findFeedList = feedRepository.findByCommentsAuthorIdOrderByCommentsIdDesc(userId, PageRequest.of(0, limit));
        return getUserCommentFeedsResponse(findFeedList);
    }

    private static UserFeedsResponse getUserFeedsResponse(Slice<Feed> findFeedList) {
        return UserFeedsResponse.builder()
                .last(findFeedList.isLast())
                .feedList(findFeedList.stream().toList())
                .build();
    }

    private static UserCommentFeedsResponse getUserCommentFeedsResponse(Slice<Feed> findFeedList) {
        return UserCommentFeedsResponse.builder()
                .last(findFeedList.isLast())
                .feedList(findFeedList.stream().toList())
                .build();
    }
}
