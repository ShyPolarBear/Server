package com.shy_polarbear.server.domain.comment.controller;

import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.comment.service.CommentService;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;


    private final CommentRepository commentRepository;

    public CommentController(CommentService commentService,
                             CommentRepository commentRepository){
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    // 댓글 작성


    // 댓글 조회


    // 댓글 조회



    // 댓글 수정


    // 댓글 삭제


    // 댓글 좋아요

    // 댓글 신고

}
