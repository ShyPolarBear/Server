package com.shy_polarbear.server.domain.comment.controller;

import com.shy_polarbear.server.domain.comment.dto.CommentDto;
import com.shy_polarbear.server.domain.comment.mapper.CommentMapper;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentCursorResult;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.comment.service.CommentService;
import com.shy_polarbear.server.domain.feed.model.Feed;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    public CommentController(CommentService commentService, CommentMapper commentMapper,
                             CommentRepository commentRepository){
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> postComment(@RequestBody CommentDto.Post postComment){

        Comment comment = commentMapper.commentPostToComment(postComment);

        return new ResponseEntity<>(
                commentMapper.commentToCommentResponseDto(commentService.createComment(comment)),
                HttpStatus.CREATED);
    }

    // 댓글 조회
//    @GetMapping
//    public CommentCursorResult<Comment> getComments(Long cursorId, Integer size){
//        if (size == null) size = DEFAULT_SIZE;
//
//        return this.commentService.getClass(cursorId, PageRequest.of(0, size));
//    }


    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<?> putComment(@PathVariable("commentId") Long commentId,
                                     @RequestBody CommentDto.Patch patchComment){

        Comment updatedComment = commentService.updateComment(commentId, patchComment);
        CommentDto.Response responseDto = commentMapper.commentToCommentResponseDto(updatedComment);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") @Positive Long commentId){
        commentService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 댓글 좋아요

    // 댓글 신고
    @GetMapping("/{commentId}/repost")
    public ResponseEntity<CommentDto.Response> repostComment(@PathVariable("commentId") Long commentId) {

        // commentId를 가지고 온다.
        Comment reportedComment = commentService.reportComment(commentId);

        // 상태가 변경된 댓글을 Response 해준다.
        CommentDto.Response responseDto = commentMapper.commentToCommentResponseDto(reportedComment);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
