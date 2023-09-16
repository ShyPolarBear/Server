package com.shy_polarbear.server.domain.comment.service;

import com.shy_polarbear.server.domain.comment.dto.request.CommentCreateRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CommentCreateResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentResponse;
import com.shy_polarbear.server.domain.comment.exception.CommentException;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.NoCountPageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;
    private final FeedService feedService;
    private final CommentRepository commentRepository;

    // 댓글 리스트 조회 (무한 스크롤)
    public NoCountPageResponse<CommentResponse> getComments(Long currentUserId, Long feedId, Long cursorId, int limit) {
        Slice<Comment> parentComments = commentRepository.findAllParentComment(currentUserId, feedId, cursorId, limit);
        Slice<CommentResponse> result = parentComments.map(
                it -> CommentResponse.of(it, it.isAuthor(currentUserId), it.isLike(currentUserId), it.getChildComments(), currentUserId)
        );

        return NoCountPageResponse.of(result);
    }

    // 댓글 생성
    @Transactional
    public CommentCreateResponse createComment(Long currentUserId, Long feedId, CommentCreateRequest request) {
        User user = userService.getUser(currentUserId);
        Feed feed = feedService.findFeedById(feedId);

        Long parentId = request.getParentId();
        if (Objects.isNull(parentId)) {    // 부모 댓글
            Comment comment = commentRepository.save(Comment.createComment(user, request.getContent(), feed));
            return CommentCreateResponse.ofParent(comment);
        } else {    // 자식 댓글
            Comment parent = commentRepository.findById(parentId).orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
            Comment comment = commentRepository.save(Comment.createChildComment(user, request.getContent(), feed, parent));
            return CommentCreateResponse.ofChild(comment);
        }
    }

    // 댓글 수정


    // 댓글 좋아요 혹은 좋아요 취소


    // 댓글 삭제
}
