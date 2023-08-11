package com.shy_polarbear.server.domain.comment.controller;

import com.shy_polarbear.server.domain.comment.dto.request.CreateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.*;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CursorResult;
import com.shy_polarbear.server.domain.comment.service.CommentService;

import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private static final int DEFAULT_SIZE = 10;

    // 댓글 작성
    @PostMapping("/{feedId}")
    public ApiResponse<CreateCommentResponse> createComment(@PathVariable Long feedId, @RequestBody CreateCommentRequest createCommentRequest) {
        Long parentId = createCommentRequest.getParentId();
        if (parentId == null) {
            return ApiResponse.success(commentService.createComment(feedId, createCommentRequest));
        }
        return ApiResponse.success(commentService.createChildComment(feedId, createCommentRequest));
    }

    // 댓글 조회
    @GetMapping("/{feedId}")
    public ApiResponse<CursorResult<Comment>> findCommentsByFeedId(@PathVariable Long feedId,
                                                      @RequestParam(required = false) Long cursorId,
                                                      @RequestParam(required = false) Integer size){
        if (size == null) size = DEFAULT_SIZE;

        Pageable pageable = PageRequest.of(0, size);
        CursorResult<Comment> cursorResult = commentService.getCommentsByFeedId(feedId, cursorId, pageable);

        return ApiResponse.success(cursorResult);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<UpdateCommentResponse> updateComment(@RequestBody UpdateCommentRequest updateCommentRequest,
                                                            @PathVariable Long commentId){
        return ApiResponse.success(commentService.updateComment(updateCommentRequest, commentId));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentDeleteResponse> deleteComment(@PathVariable Long commentId) {
        return ApiResponse.success(commentService.deleteComment(commentId));
    }

    // 댓글 좋아요
    @PutMapping("/{commentId}/like")
    public ApiResponse<CommentLikeResponse> likeComment(@PathVariable Long commentId, @RequestParam Long userId){
        return ApiResponse.success(commentService.likeComment(commentId, userId));
    }
}
