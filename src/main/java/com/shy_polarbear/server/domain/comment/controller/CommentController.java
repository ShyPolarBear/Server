package com.shy_polarbear.server.domain.comment.controller;

import com.shy_polarbear.server.domain.comment.dto.request.CommentCreateRequest;
import com.shy_polarbear.server.domain.comment.dto.request.CommentUpdateRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CommentCreateResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentLikeResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentUpdateResponse;
import com.shy_polarbear.server.domain.comment.service.CommentService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetails;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.common.dto.NoCountPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shy_polarbear.server.global.common.dto.ApiResponse.success;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 리스트 조회 (무한 스크롤)
    @GetMapping("/{feedId}")
    public ApiResponse<NoCountPageResponse<CommentResponse>> getComments(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(required = false) Long lastCommentId,
            @RequestParam(required = false, defaultValue = BusinessLogicConstants.COMMENT_LIMIT_PARAM_DEFAULT_VALUE) Integer limit
    ) {
        NoCountPageResponse<CommentResponse> response = commentService.getComments(principalDetails.getUser().getId(), feedId, lastCommentId, limit);
        return success(response);
    }

    // 댓글 생성
    @PostMapping("/{feedId}")
    public ApiResponse<CommentCreateResponse> createComment(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        CommentCreateResponse response = commentService.createComment(principalDetails.getUser().getId(), feedId, request);
        return success(response);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ApiResponse<CommentUpdateResponse> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        CommentUpdateResponse response = commentService.updateComment(principalDetails.getUser().getId(), commentId, request);
        return success(response);
    }


    // 댓글 좋아요 혹은 좋아요 취소
    @PutMapping("/{commentId}/like")
    public ApiResponse<CommentLikeResponse> likeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        CommentLikeResponse response = commentService.likeComment(principalDetails.getUser().getId(), commentId);
        return success(response);
    }


    // 댓글 삭제
}
