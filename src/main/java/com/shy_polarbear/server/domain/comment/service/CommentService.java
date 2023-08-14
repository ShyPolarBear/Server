package com.shy_polarbear.server.domain.comment.service;



import com.shy_polarbear.server.domain.comment.dto.request.CreateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.*;
import com.shy_polarbear.server.domain.comment.model.*;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.comment.exception.CommentException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.PageResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final FeedRepository feedRepository;


    @PersistenceContext
    private EntityManager entityManager;


    // 댓글 등록하기
    public CreateCommentResponse createComment(Long feedId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurruentUser();
        Feed feed = getFeedById(feedId);
        Comment comment = createNewComment(user, createCommentRequest.getContent(), feed, CommentType.COMMENT);
        commentRepository.save(comment);

        return new CreateCommentResponse(comment.getId(), null);
    }

    // 대댓글 등록하기
    public CreateCommentResponse createChildComment(Long feedId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurruentUser();
        Feed feed = getFeedById(feedId);

        Long parentId = createCommentRequest.getParentId();
        Comment parentComment = getParentCommentById(createCommentRequest.getParentId());

        Comment comment = createNewChildComment(user, createCommentRequest.getContent(), feed, parentComment, CommentType.CHILD_COMMENT);
        commentRepository.save(comment);

        return new CreateCommentResponse(comment.getId(), parentId);
    }

    // 댓글 조회
    public CommentPageResponse findAllComments(Long feedId){
        Slice<Comment> commentSlice = commentRepository.findByFeedIdOrderByCreatedAtDesc(feedId);

        List<Comment> comments = commentSlice.getContent();
        boolean isLast = !commentSlice.hasNext();
        User currentUser = userService.getCurruentUser(); // 현재 사용자 정보를 가져와야 함

        CommentPageResponse response = CommentPageResponse.builder()
                .isLast(isLast)
                .commentList(comments)
                .currentUser(currentUser)
                .build();

        return response;
    }

    public CommentPageResponse findCommentsByFeedIdWithCursor(Long feedId, Long lastCommentId, int size){
        PageRequest pageRequest = PageRequest.of(0, size);

        User user = userService.getCurruentUser();

        // 페이지네이션된 댓글 목록 가져오기
        Slice<Comment> commentSlice = commentRepository.findCommentsByFeedIdWithCursor(feedId, lastCommentId, pageRequest);

        // 다음 페이지가 있는지 여부 판단
        boolean isLast = !commentSlice.hasNext();

        //댓글 페이지 응답 생성
        return CommentPageResponse.builder()
                .isLast(isLast)
                .commentList(commentSlice.getContent())
                .currentUser(user)
                .build();
    }

    public CommentPageResponse findChildCommentsByParentIdWithCursor(Long parentId, Long lastComment, int size){
        PageRequest pageRequest = PageRequest.of(0, size);

        User user = userService.getCurruentUser();

        Slice<Comment> childCommentSlice = commentRepository.findChildCommentsByParentIdWithCursor(parentId, lastComment, pageRequest);

        boolean isLast = !childCommentSlice.hasNext();

        return CommentPageResponse.builder()
                .isLast(isLast)
                .commentList(childCommentSlice.getContent())
                .currentUser(user)
                .build();
    }

    // 댓글 수정
    public UpdateCommentResponse updateComment(UpdateCommentRequest updateCommentRequest, Long commentId) {
        // 현재 사용자 정보 가져오기
        User findUser = userService.getCurruentUser();
        // 댓글 조회
        Comment findComment = findComment(commentId);
        // 본인이 작성한 댓글인지 확인
        if (!findComment.getAuthor().equals(findUser)){
            throw new CommentException(ExceptionStatus.NOT_MY_COMMENT_UPDATE);
        }
        // 댓글 내용 업데이트
        findComment.updateContent(updateCommentRequest.getContent());

        return UpdateCommentResponse.from(findComment);
    }

    // 댓글 삭제
    public CommentDeleteResponse deleteComment(Long commentId) {
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment == null) {
            throw new CommentException(ExceptionStatus.NOT_FOUND_COMMENT);
        }

        User currentUser = userService.getCurruentUser();
        if (!existingComment.getAuthor().equals(currentUser)) {
            throw new CommentException(ExceptionStatus.NOT_MY_COMMENT_DELETE);
        }

        commentRepository.delete(existingComment);

        CommentDeleteResponse response = new CommentDeleteResponse(commentId);

        return response;
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
        CommentLike commentLike = CommentLike.createCommentLike(existingComment, user);
        entityManager.persist(commentLike);
        existingComment.addLike(commentLike);

        commentRepository.save(existingComment);

        return new CommentLikeResponse(commentLike);
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

    private Comment createNewComment(User user, String content, Feed feed, CommentType comment) {
        return Comment.createComment(user, content, feed, comment);
    }

    private Comment createNewChildComment(User user, String content, Feed feed, Comment parentComment, CommentType childComment) {
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
