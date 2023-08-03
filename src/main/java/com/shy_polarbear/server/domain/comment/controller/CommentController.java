package com.shy_polarbear.server.domain.comment.controller;

import com.shy_polarbear.server.domain.comment.dto.request.CommentPageRequest;
import com.shy_polarbear.server.domain.comment.dto.request.CreateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CommentPageResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CreateCommentResponse;
import com.shy_polarbear.server.domain.comment.dto.response.GetCommentResponse;
import com.shy_polarbear.server.domain.comment.service.CommentService;

import com.shy_polarbear.server.global.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/feeds/{feedId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping
    public ApiResponse<CreateCommentResponse> createComment(@PathVariable Long feedId, @RequestBody CreateCommentRequest createCommentRequest) {
        Long parentId = createCommentRequest.getParentId();
        if (parentId == null) {
            return ApiResponse.success(commentService.createComment(feedId, createCommentRequest));
        }
        return ApiResponse.success(commentService.createChildComment(feedId, createCommentRequest));
    }

    // 댓글 조회
    @GetMapping
    public ApiResponse<CommentPageResponse> getCommentList(@RequestBody CommentPageRequest request) {
        List<GetCommentResponse.CommentInfo> comments = commentService.getComments(
                request.getFeedId(),
                request.getPageNumber(),
                request.getPageSize()
        );

        // 서비스에서 hasNextPage 정보를 계산하여 전달하거나, 필요한 로직을 추가하여 계산합니다.
        boolean hasNextPage = commentService.hasNextPage(request.getFeedId(), request.getPageNumber(), request.getPageSize());

        CommentPageResponse response = new CommentPageResponse(comments, hasNextPage);

        return ApiResponse.success(response);
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
