package com.shy_polarbear.server.domain.comment.service;



import com.shy_polarbear.server.domain.comment.dto.request.CommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.UpdateCommentResponse;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import com.shy_polarbear.server.domain.comment.model.CommentReport;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final FeedService feedService;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository,
                          UserService  userService, FeedService feedService){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.feedService = feedService;
    }


    // 댓글 등록하기
    public Comment postComment(CommentRequest commentRequest, User author, Feed feedId) {

        User user = userService.getCurruentUser();

//        Feed feed = feedService.getFeedById(feedId);

        Comment newComment = Comment.createComment(author, commentRequest.getContent(), feedId);

        return commentRepository.save(newComment);
    }


    // 댓글 조회
    public Comment getComment(Long commentId) {

        return commentRepository.findById(commentId).orElse(null);
    }

    // 댓글 수정
    public UpdateCommentResponse updateComment(UpdateCommentRequest updateCommentRequest, Long commentId) {
        // 현재 사용자 정보 가져오기
        User findUser = userService.getCurruentUser();
        // 댓글 조회
        Comment findComment = findComment(commentId);
        // 본인이 작성한 댓글인지 확인
        if (!findComment.getAuthor().equals(findUser)){
            throw new CustomException(ExceptionStatus.NOT_MY_COMMENT);
        }
        // 댓글 내용 업데이트
        findComment.updateContent(updateCommentRequest.getContent());

        return UpdateCommentResponse.from(findComment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {

        User findUser = userService.getCurruentUser();

        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            commentRepository.delete(existingComment);
        }
    }

    // 댓글 좋아요
    public void likeComment(Long commentId, Long userId) {

        // 좋아요를 누르는 유저
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ExceptionStatus.NOT_FOUND_USER));
        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            // 이미 해당 사용자가 댓글에 좋아요를 누른 경우, 중복 추가를 막기 위해 체크
            if (existingComment.getCommentLikes().stream().noneMatch(like -> like.getUser().equals(user))) {
                CommentLike commentLike = new CommentLike(user, existingComment);
                existingComment.addLike(commentLike);
                commentRepository.save(existingComment);
            }
        } else {
            throw new CustomException(ExceptionStatus.NOT_FOUND_COMMENT);
        }
    }
    // 댓글 신고
    public void reportComment(Long commentId, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ExceptionStatus.NOT_FOUND_USER));

        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ExceptionStatus.NOT_FOUND_COMMENT));

        if (existingComment != null) {
            // 본인의 댓글 신고할 경우
            if (existingComment.getAuthor().equals(user)){
                throw new CustomException(ExceptionStatus.NOT_MY_COMMENT_REPORT);
            }
            // 이미 신고한 댓글인 경우
            if (existingComment.getCommentReports().stream().anyMatch(report -> report.getUser().equals(user))){
                throw new CustomException(ExceptionStatus.COMMENT_REPORT_DUPLICATION);
            }

            // 댓글에 신고 정보 추가
            existingComment.addReport(new CommentReport(user));
            commentRepository.save(existingComment);
        } else {
            // 댓글이 존재하지 않는 경우
            throw new CustomException(ExceptionStatus.NOT_FOUND_COMMENT);
        }
    }

    private Comment findComment(Long commentId) {
        Optional<Comment> findComment = commentRepository.findById(commentId);

        return findComment.orElseThrow(() -> new CustomException(ExceptionStatus.NOT_FOUND_COMMENT));
    }

}
