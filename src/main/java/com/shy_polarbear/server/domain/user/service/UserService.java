package com.shy_polarbear.server.domain.user.service;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.dto.user.request.UpdateUserInfoRequest;
import com.shy_polarbear.server.domain.user.dto.user.response.*;
import com.shy_polarbear.server.domain.user.exception.DuplicateNicknameException;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.auth.security.SecurityUtils;
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

    public Long save(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public UserInfoResponse findUserInfo() {
        User findUser = getCurruentUser();
        return UserInfoResponse.from(findUser);
    }

    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest userInfoRequest) {
        User findUser = getCurruentUser();
        if (!findUser.isSameNickName(userInfoRequest.getNickName())) {
            checkDuplicateNickName(userInfoRequest.getNickName());
        }
        findUser.updateInfo(userInfoRequest.getNickName(), userInfoRequest.getProfileImage(), userInfoRequest.getEmail(), userInfoRequest.getPhoneNumber());
        return UpdateUserInfoResponse.from(findUser);
    }

    public User getCurruentUser() {
        String providerId = SecurityUtils.getLoginUserProviderId();
        User findUser = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
        return findUser;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
    }

    public UserFeedsResponse findUserFeedsByCursorId(Long lastFeedId, Integer limit) {
        User user = getCurruentUser();
        Slice<Feed> findFeedList = feedRepository.findByIdLessThanAndAuthorOrderByIdDesc(lastFeedId, user, PageRequest.of(0, limit));
        return getUserFeedsResponse(findFeedList);
    }

    public UserFeedsResponse findUserFeeds(Integer limit) {
        User user = getCurruentUser();
        Slice<Feed> findFeedList = feedRepository.findByAuthorOrderByIdDesc(user, PageRequest.of(0, limit));
        return getUserFeedsResponse(findFeedList);
    }

    public UserCommentFeedsResponse findUserCommentFeedsByCursorId(Long lastCommentId, Integer limit) {
        User user = getCurruentUser();
        Slice<Feed> findFeedList = feedRepository.findByCommentsAuthorAndCommentsIdLessThanOrderByCommentsIdDesc(user, lastCommentId, PageRequest.of(0, limit));
        return getUserCommentFeedsResponse(findFeedList);
    }

    public UserCommentFeedsResponse findUserCommentFeeds(Integer limit) {
        User user = getCurruentUser();
        Slice<Feed> findFeedList = feedRepository.findByCommentsAuthorOrderByCommentsIdDesc(user, PageRequest.of(0, limit));
        return getUserCommentFeedsResponse(findFeedList);
    }

    private static UserFeedsResponse getUserFeedsResponse(Slice<Feed> findFeedList) {
        return UserFeedsResponse.builder()
                .isLast(findFeedList.isLast())
                .feedList(findFeedList.stream().toList())
                .build();
    }

    private static UserCommentFeedsResponse getUserCommentFeedsResponse(Slice<Feed> findFeedList) {
        return UserCommentFeedsResponse.builder()
                .isLast(findFeedList.isLast())
                .feedList(findFeedList.stream().toList())
                .build();
    }
}
