package com.shy_polarbear.server.domain.comment.service;

import com.shy_polarbear.server.domain.comment.dto.CommentDto;
import com.shy_polarbear.server.domain.comment.mapper.CommentMapper;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentCursorResult;
import com.shy_polarbear.server.domain.comment.model.CommentStatus;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper){
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    // 댓글 등록하기
    public Comment createComment(Comment comment){

        comment.setCommentStatus(CommentStatus.ENGAGED);

        return commentRepository.save(comment);
    }

    // 댓글 수정하기
    public Comment updateComment(Long commentId, CommentDto.Patch patchComment){

        Comment findComment = findExistedComment(commentId);

        findComment.setContent(patchComment.getContent());

        return commentRepository.save(findComment);
    }

    // CommentId로 조회하기
    public Comment findComment(Long commentId) {

        Comment comment = findExistedComment(commentId);

        return comment;
    }



    // 모든 댓글 조회
    public List<Comment> findComments(CommentCursorResult cursorResult) {
        return null;
    }

    // CommentId로 삭제하기
    public void deleteComment(Long commentId){

        commentRepository.delete(findExistedComment(commentId));
    }

    // 댓글 신고하기
    public Comment reportComment(Long commentId){

        // commentId로 comment를 찾아 status를 신고당함으로 바꾸기
        Comment comment = findExistedComment(commentId);
        comment.setCommentStatus(CommentStatus.REPORTED);

        return commentRepository.save(comment);
    }


    private Comment findExistedComment(Long commentId){

        // commentId로
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        return optionalComment.orElseThrow(
                () -> new CustomException(ExceptionStatus.NOT_FOUND_COMMENT)
        );
    }
}
