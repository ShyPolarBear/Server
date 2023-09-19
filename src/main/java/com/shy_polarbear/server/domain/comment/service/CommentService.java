package com.shy_polarbear.server.domain.comment.service;

import com.shy_polarbear.server.domain.comment.dto.request.CommentCreateRequest;
import com.shy_polarbear.server.domain.comment.dto.request.CommentUpdateRequest;
import com.shy_polarbear.server.domain.comment.dto.response.*;
import com.shy_polarbear.server.domain.comment.exception.CommentException;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import com.shy_polarbear.server.domain.comment.repository.CommentLikeRepository;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
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
import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;
    private final FeedService feedService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

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
    @Transactional
    public CommentUpdateResponse updateComment(Long currentUserId, Long commentId, CommentUpdateRequest request) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
        checkIsAuthor(user, comment);

        comment.update(request.getContent());

        return CommentUpdateResponse.of(comment);
    }

    // 댓글 좋아요 혹은 좋아요 취소
    @Transactional
    public CommentLikeResponse likeComment(Long currentUserId, Long commentId) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
        Optional<CommentLike> commentLikeAble = commentLikeRepository.findByUserAndComment(user, comment);

        boolean isLiked;
        if (commentLikeAble.isEmpty()) {
            CommentLike commentLike = CommentLike.createCommentLike(comment, user);
            comment.addLike(commentLike);
            isLiked = true;
        } else {
            commentLikeRepository.delete(commentLikeAble.get());
            isLiked = false;
        }

        return CommentLikeResponse.of(isLiked);
    }


    // 댓글 삭제
    @Transactional
    public CommentDeleteResponse deleteComment(Long currentUserId, Long commentId) {
        User user = userService.getUser(currentUserId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
        if (!comment.getVisibility()) throw new CommentException(ExceptionStatus.NOT_FOUND_COMMENT);

        checkIsAuthor(user, comment);

        comment.softDelete();   // 논리적 삭제

        return CommentDeleteResponse.of(comment.getId());
    }


    private static void checkIsAuthor(User user, Comment comment) {
        if (!comment.isAuthor(user.getId()))
            throw new CommentException(ExceptionStatus.NOT_MY_COMMENT);
    }
}
