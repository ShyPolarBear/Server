package com.shy_polarbear.server.domain.comment.service;

import com.shy_polarbear.server.domain.comment.dto.request.CommentCreateRequest;
import com.shy_polarbear.server.domain.comment.dto.request.CommentUpdateRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CommentCreateResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentLikeResponse;
import com.shy_polarbear.server.domain.comment.dto.response.CommentUpdateResponse;
import com.shy_polarbear.server.domain.comment.exception.CommentException;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import com.shy_polarbear.server.domain.comment.repository.CommentLikeRepository;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.comment.template.CommentTemplate;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.feed.template.FeedTemplate;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.domain.user.template.UserTemplate;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    UserService userService;
    @Mock
    FeedService feedService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    CommentLikeRepository commentLikeRepository;


    private final User dummyUser = UserTemplate.createDummyUser();
    private final User dummyOtherUserA = UserTemplate.createDummyOtherUserA();
    private final Feed dummyFeed = FeedTemplate.createDummyFeed();
    private final Comment dummyParentComment = CommentTemplate.createDummyParentComment();
    private final Comment dummyChildComment = CommentTemplate.createDummyChildComment(dummyParentComment);


    @Test
    @DisplayName("createComment 성공")
    public void createCommentSuccess() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(feedService.findFeedById(anyLong())).willReturn(dummyFeed);
        given(commentRepository.save(any())).willReturn(dummyParentComment);

        // when
        CommentCreateRequest request = CommentCreateRequest.builder().content("부모 댓글").build();
        CommentCreateResponse response = commentService.createComment(UserTemplate.ID, 1L, request);

        // then
        assertThat(response.parentId()).isNull();
        assertThat(response.commentId()).isEqualTo(CommentTemplate.PARENT_ID);
    }

    @Test
    @DisplayName("createComment 실패: 존재하지 않는 부모 댓글")
    public void createCommentNotFoundFeedFail() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(feedService.findFeedById(anyLong())).willReturn(dummyFeed);
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());  // 예외 발생

        // when & then
        CommentCreateRequest request = CommentCreateRequest.builder().content("자식 댓글").parentId(anyLong()).build();
        assertThatThrownBy(() -> commentService.createComment(UserTemplate.ID, 1L, request))
                .isInstanceOf(CommentException.class)
                .hasMessage(ExceptionStatus.NOT_FOUND_COMMENT.getMessage());
    }

    // 업데이트 성공
    // 업데이트 실패: 존재하지 않는 댓글, 작성자가 아닌 경우
    @Test
    @DisplayName("updateComment 성공")
    public void updateCommentSuccess() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(dummyParentComment));

        // when
        CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정된 댓글").build();
        CommentUpdateResponse response = commentService.updateComment(UserTemplate.ID, dummyParentComment.getId(), request);

        // then
        assertThat(response.commentId()).isEqualTo(dummyParentComment.getId());
    }

    @Test
    @DisplayName("updateComment 실패: 존재하지 않는 댓글")
    public void updateCommentNotFoundFail() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정된 내용").build();
        assertThatThrownBy(() -> commentService.updateComment(UserTemplate.ID, dummyParentComment.getId(), request))
                .isInstanceOf(CommentException.class)
                .hasMessage(ExceptionStatus.NOT_FOUND_COMMENT.getMessage());
    }

    @Test
    @DisplayName("updateComment 실패: 작성자와 유저가 일치하지 않음")
    public void updateCommentNotAuthorFail() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyOtherUserA);  // 예외 발생: dummyParentComment.User 와 다른 유저
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(dummyParentComment));

        // when & then
        CommentUpdateRequest request = CommentUpdateRequest.builder().content("수정된 내용").build();
        assertThatThrownBy(() -> commentService.updateComment(UserTemplate.ID, dummyParentComment.getId(), request))
                .isInstanceOf(CommentException.class)
                .hasMessage(ExceptionStatus.NOT_MY_COMMENT.getMessage());
    }

    @Test
    @DisplayName("likeComment 성공: 좋아요")
    public void likeCommentMakeLikeSuccess() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(dummyParentComment));
        given(commentLikeRepository.findByUserAndComment(dummyUser, dummyParentComment)).willReturn(Optional.empty());

        // when
        CommentLikeResponse response = commentService.likeComment(UserTemplate.ID, CommentTemplate.PARENT_ID);

        // then
        assertThat(response.isLiked()).isTrue();
    }

    @Test
    @DisplayName("likeComment 성공: 좋아요 취소")
    public void likeCommentCancelLikeSuccess() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(dummyParentComment));
        CommentLike commentLike = CommentLike.createCommentLike(dummyParentComment, dummyUser);
        given(commentLikeRepository.findByUserAndComment(dummyUser, dummyParentComment)).willReturn(Optional.of(commentLike));

        // when
        CommentLikeResponse response = commentService.likeComment(UserTemplate.ID, CommentTemplate.PARENT_ID);

        // then
        assertThat(response.isLiked()).isFalse();
    }

    @Test
    @DisplayName("likeComment 실패: 존재하지 않는 댓글")
    public void likeCommentNotFoundFail() {
        // given
        given(userService.getUser(anyLong())).willReturn(dummyUser);
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());  // 예외 발생

        // when & then
        assertThatThrownBy(()-> commentService.likeComment(UserTemplate.ID, CommentTemplate.PARENT_ID))
                .isInstanceOf(CommentException.class)
                .hasMessage(ExceptionStatus.NOT_FOUND_COMMENT.getMessage());
    }

    // 삭제 성공: 소프트 딜리트
    // 삭제 실패: 존재하지 않는 댓글, 작성자가 아닌 경우


}
