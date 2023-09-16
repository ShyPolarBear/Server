package com.shy_polarbear.server.global.common.dummy;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.exception.UserException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.global.common.constants.ProfileConstants;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.util.List;

import static com.shy_polarbear.server.global.common.dummy.LoginUserInitializer.LOGIN_USER_PROVIDER_ID;

@Component("CommentInitializer")
@DependsOn({"FeedInitializer"})
@RequiredArgsConstructor
@Slf4j
@Transactional
@Profile({ProfileConstants.LOCAL, ProfileConstants.DEV})
public class CommentInitializer {
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (commentRepository.count() == 0) {
            createDummyComment();
            log.info("[CommentInitializer] 댓글 생성 완료");
        } else {
            log.info("[CommentInitializer] 댓글 이미 존재");
        }
    }

    private void createDummyComment() {
        User author = userRepository.findByProviderId(LOGIN_USER_PROVIDER_ID)
                .orElseThrow(() -> new UserException(ExceptionStatus.NOT_FOUND_USER));
        List<User> userList = userRepository.findAll();
        List<Feed> feedList = feedRepository.findAll();

        for (Feed feed : feedList) {
            for (int i = 0; i < 25; i++) {
                Comment parent = Comment.createComment(author, "부모 댓글" + i, feed);
                for (User user : userList) {
                    CommentLike.createCommentLike(parent, user);    // 댓글 좋아요
                }
                commentRepository.save(parent);

                for (int j = 0; j < 5; j++) {
                    commentRepository.save(Comment.createChildComment(author, "자식 댓글" + i, feed, parent));
                }
            }
        }

    }

}
