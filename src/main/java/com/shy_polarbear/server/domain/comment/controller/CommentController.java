package com.shy_polarbear.server.domain.comment.controller;

import com.shy_polarbear.server.domain.comment.dto.request.CommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CommentResponse;
import com.shy_polarbear.server.domain.comment.dto.response.UpdateCommentResponse;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.comment.service.CommentService;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/feeds/{feedId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping
    public ApiResponse<?> createComment(@RequestBody CommentRequest commentRequest, User author, Feed feedId) {
        return ApiResponse.success(commentService.postComment(commentRequest, author, feedId));
    }

    // 댓글 조회
    @GetMapping
    public ApiResponse<?> getComment(@RequestBody CommentResponse commentResponse){
        return ApiResponse.success(commentResponse);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<?> putComment(@RequestBody UpdateCommentRequest updateCommentRequest, Long commentId){
        return ApiResponse.success(commentService.updateComment(updateCommentRequest, commentId));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<?> deleteComment(@PathVariable Long commentId){
            commentService.deleteComment(commentId);
            return ApiResponse.success(null);
        }

    // 댓글 좋아요
    @PutMapping("/{commentId}/like")
    public ApiResponse<?> likeComment(@PathVariable Long commentId, @RequestParam Long userId){
        commentService.likeComment(commentId, userId);
        return ApiResponse.success(null);
    }

    // 댓글 신고
    @GetMapping("/{commentId}/report")
    public ApiResponse<?> reportComment(@PathVariable Long commentId, @RequestParam Long userId){
        commentService.reportComment(commentId, userId);
        return ApiResponse.success(null);
    }
}
