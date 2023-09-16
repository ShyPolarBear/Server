package com.shy_polarbear.server.domain.comment.service;

import com.shy_polarbear.server.domain.comment.dto.CommentResponse;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.common.dto.NoCountPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;
    private final CommentRepository commentRepository;

    // 댓글 리스트 조회 (무한 스크롤)
    public NoCountPageResponse<CommentResponse> getComments(Long currentUserId, Long feedId, Long cursorId, int limit) {
        Slice<Comment> parentComments = commentRepository.findAllParentComment(currentUserId, feedId, cursorId, limit);
        Slice<CommentResponse> result = parentComments.map(
                it -> CommentResponse.of(it, it.isAuthor(currentUserId), it.isLike(currentUserId), it.getChildComments(), currentUserId)
        );

        return NoCountPageResponse.of(result);
    }

    // 댓글 생성


    // 댓글 수정


    // 댓글 좋아요 혹은 좋아요 취소


    // 댓글 삭제
}
