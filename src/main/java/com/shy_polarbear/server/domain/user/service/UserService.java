package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.*;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

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

    public PageResponse<UserFeedResponse> findUserFeeds(Long lastFeedId, Integer limit, Long userId) {
        Slice<Feed> userFeeds = feedRepository.findUserFeeds(lastFeedId, limit, userId);
        Slice<UserFeedResponse> userFeedResponses = userFeeds.map(feed -> UserFeedResponse.from(feed));
        return PageResponse.of(userFeedResponses, userFeedResponses.stream().count());
    }

    public PageResponse<UserCommentFeedResponse> findAllFeedsByUserComment(Long lastCommentId, Integer limit, Long userId) {
        Slice<Comment> userCommentsInFeed = commentRepository.findRecentUserCommentsInFeed(lastCommentId, limit, userId);
        Slice<UserCommentFeedResponse> userCommentFeedResponses = userCommentsInFeed
                .map(comment -> UserCommentFeedResponse.from(comment.getFeed(), comment.getId()));
        return PageResponse.of(userCommentFeedResponses, userCommentFeedResponses.stream().count());
    }
}
