package com.shy_polarbear.server.domain.comment.service;



import com.shy_polarbear.server.domain.comment.dto.request.CreateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CommentLikeResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentPageResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CreateCommentResponse;
import com.shy_polarbear.server.domain.comment.dto.response.UpdateCommentResponse;
import com.shy_polarbear.server.domain.comment.model.*;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.comment.exception.CommentException;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final FeedRepository feedRepository;

    private final FeedService feedService;


    // 댓글 등록하기
    public CreateCommentResponse createComment(Long feedId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurruentUser();
        Feed feed = getFeedById(feedId);
        Comment comment = createNewComment(user, createCommentRequest.getContent(), feed, CommentInfo.COMMENT);
        commentRepository.save(comment);

        return new CreateCommentResponse(comment.getId(), null);
    }

    // 대댓글 등록하기
    public CreateCommentResponse createChildComment(Long feedId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurruentUser();
        Feed feed = getFeedById(feedId);

        Long parentId = createCommentRequest.getParentId();
        Comment parentComment = getParentCommentById(createCommentRequest.getParentId());

        Comment comment = createNewChildComment(user, createCommentRequest.getContent(), feed, parentComment, CommentInfo.CHILD_COMMENT);
        commentRepository.save(comment);

        return new CreateCommentResponse(comment.getId(), parentId);
    }

    // 댓글 조회
    public CursorResult<Comment> getCommentsByFeedId (Long feedId, Long cursorId, Pageable page){
        List<Comment> comments = getComments(feedId, cursorId, page);
        Long lastIdOfList = comments.isEmpty() ? null : comments.get(comments.size() - 1).getId();
        boolean hasNext = hasNext(lastIdOfList);

        return new CursorResult<>(comments, hasNext);
    }

    private List<Comment> getComments(Long feedId, Long commentId, Pageable page){
        Feed findFeed = getFeedById(feedId);


        return commentId == null ?
                this.commentRepository.findAllByFeedIdOrderByCreatedAt(findFeed.getId(), page) :
                this.commentRepository.findAllByFeedIdAndIdLessThanOrderByCreatedAtDesc(findFeed.getId(), commentId, page);
    }

    private Boolean hasNext(Long commentId){
        if (commentId == null) return false;
        return this.commentRepository.existsByIdLessThan(commentId);
    }

    // 댓글 수정
    public UpdateCommentResponse updateComment(UpdateCommentRequest updateCommentRequest, Long commentId) {
        // 현재 사용자 정보 가져오기
        User findUser = userService.getCurruentUser();
        // 댓글 조회
        Comment findComment = findComment(commentId);
        // 본인이 작성한 댓글인지 확인
        if (!findComment.getAuthor().equals(findUser)){
            throw new CommentException(ExceptionStatus.NOT_MY_COMMENT);
        }
        // 댓글 내용 업데이트
        findComment.updateContent(updateCommentRequest.getContent());

        return UpdateCommentResponse.from(findComment);
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId) {
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment == null) {
            return false;
        }

        User currentUser = userService.getCurruentUser();
        if (!existingComment.getAuthor().equals(currentUser)) {
            return false;
        }

        commentRepository.delete(existingComment);
        return true;
    }

    // 댓글 좋아요
    public CommentLikeResponse likeComment(Long commentId, Long userId) {
        // 좋아요를 누르는 유저
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_USER));
        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));

        // 이미 좋아요를 누른경우
        if (isCommentAlreadyLiked(existingComment, user)) {
            throw new CommentException(ExceptionStatus.ALREADY_LIKED_COMMENT);
        }

        // commentLike 객체 생성
        CommentLike commentLike = new CommentLike(user, existingComment);
        existingComment.addLike(commentLike);

        commentRepository.save(existingComment);

        return new CommentLikeResponse(commentLike.getId());
    }

    // 댓글 신고
    public void reportComment(Long commentId, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_USER));

        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));

        // 본인의 댓글 신고할 경우
        if (existingComment.getAuthor().equals(user)){
            throw new CommentException(ExceptionStatus.NOT_ALLOWED_SLEF_REPORT);
        }
        // 이미 신고한 댓글인 경우
        if (existingComment.getCommentReports().stream().anyMatch(report -> report.getUser().equals(user))){
            throw new CommentException(ExceptionStatus.COMMENT_REPORT_DUPLICATION);
        }

        // 댓글에 신고 정보 추가
        existingComment.addReport(new CommentReport(user));
        existingComment.reportComment();
        commentRepository.save(existingComment);
    }

    private Comment findComment(Long commentId) {
        Optional<Comment> findComment = commentRepository.findById(commentId);

        return findComment.orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
    }

    private Feed getFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_FEED));
    }

    private Comment getParentCommentById(Long parentId) {
        return commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
    }

    private Comment createNewComment(User user, String content, Feed feed, CommentInfo comment) {
        return Comment.createComment(user, content, feed, comment);
    }

    private Comment createNewChildComment(User user, String content, Feed feed, Comment parentComment, CommentInfo childComment) {
        return Comment.createChildComment(user, content, feed, parentComment, childComment);
    }

    // 좋아요를 누른 댓글인지 확인하는 메소드
    private boolean isCommentAlreadyLiked(Comment comment, User user) {
        for (CommentLike like : comment.getCommentLikes()) {
            if (like.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }
}
