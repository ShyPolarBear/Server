package com.shy_polarbear.server.domain.comment.repository;

import com.shy_polarbear.server.config.TestJpaConfig;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.model.FeedImage;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.template.UserTemplate;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DataJpaTest
@Import(TestJpaConfig.class)
public class CommentRepositoryTest {
    private User dummyUser;
    private Feed dummyFeed;

    private final int DUMMY_FEED_SIZE = 10;
    private final int DUMMY_COMMENT_PER_FEED_SIZE = 25;
    private final int DUMMY_CHILD_PER_PARENT_SIZE = 5;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() {
        this.dummyUser = userRepository.save(UserTemplate.createDummyUser());

        for (int i = 0; i < DUMMY_FEED_SIZE; i++) {
            Feed saved = feedRepository.save(Feed.createFeed(
                    "title" + i,
                    "content" + i,
                    FeedImage.createFeedImages(List.of("imageurl" + i)),
                    dummyUser
            ));
            if (i == 0) dummyFeed = saved;
        }

        for (int i = 0; i < DUMMY_COMMENT_PER_FEED_SIZE; i++) {
            Comment parent = commentRepository.save(Comment.createComment(dummyUser, "부모 댓글" + i, dummyFeed));
            for (int j = 0; j < DUMMY_CHILD_PER_PARENT_SIZE; j++) {
                commentRepository.save(Comment.createChildComment(dummyUser, "자식 댓글" + i, dummyFeed, parent));
            }

        }
    }

    @Test
    @DisplayName("INSERT 부모 댓글 성공")
    public void parentCommentInsertSuccess() {
        // given
        Comment comment = Comment.createComment(dummyUser, "댓글", dummyFeed);

        // when & then
        assertThat(comment.isParent()).isTrue();
        assertThatNoException().isThrownBy(() -> commentRepository.save(comment));
        assertThat(commentRepository.save(comment).getVisibility()).isEqualTo(true);  // 디폴트 값 확인
    }

    @Test
    @DisplayName("INSERT 자식 댓글 성공")
    public void childCommentInsertSuccess() {
        // given
        Comment parent = commentRepository.save(Comment.createComment(dummyUser, "부모 댓글", dummyFeed));
        Comment comment = Comment.createChildComment(dummyUser, "자식 댓글", dummyFeed, parent);

        // when & then
        assertThat(comment.isChild()).isTrue();
        assertThatNoException().isThrownBy(() -> commentRepository.save(comment));
        assertThat(commentRepository.save(comment).getVisibility()).isEqualTo(true);  // 디폴트 값 확인
    }

    @Test
    @DisplayName("INSERT 댓글 실패 : NOT NULL 제약 위반")
    public void commentInsertNullFieldFail() {
        // given
        Comment authorNullcomment = Comment.createComment(null, "댓글", dummyFeed);
        Comment contentNullcomment = Comment.createComment(dummyUser, null, dummyFeed);
        Comment feedNullcomment = Comment.createComment(dummyUser, "댓글", null);

        // when & then
        assertThatThrownBy(() -> commentRepository.save(authorNullcomment))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> commentRepository.save(contentNullcomment))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> commentRepository.save(feedNullcomment))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("INSERT 댓글 실패 : content 글자수 제약 위반")
    public void commentInsertLengthFieldFail() {
        // given
        final String hugeContent = String.format("%330s", "댓글");
        Comment hugeContentComment = Comment.createComment(dummyUser, hugeContent, dummyFeed);

        // when & then
        assertThatThrownBy(() -> commentRepository.save(hugeContentComment))
                .hasCauseInstanceOf(DataException.class)
                .hasStackTraceContaining("Value too long");
    }

    @Test
    @DisplayName("SOFT DELETE 댓글 성공 : 엔티티가 존재 && presence == false")
    public void softDeleteComment() {
        // given
        Comment comment = commentRepository.save(Comment.createComment(dummyUser, "댓글", dummyFeed));

        // when
        comment.softDelete();

        em.flush(); // DB에 영속성 반영 및 1차 캐시 삭제
        em.clear();
        Optional<Comment> commentAble = commentRepository.findById(comment.getId());

        // then
        assertThatNoException().isThrownBy(commentAble::get);
        assertThat(commentAble.isPresent()).isTrue();
        assertThat(commentAble.get().getVisibility()).isFalse();
    }

    @Test
    @DisplayName("findAllParentComment 성공")
    public void findAllParentCommentSuccess() {
        // given
        int limit = 10;
        long userId = UserTemplate.ID, cursorId = 25;

        // when
        Slice<Comment> parentCommentList = commentRepository.findAllParentComment(userId, dummyFeed.getId(), cursorId, limit);

        boolean isCorrectParent = true;
        boolean isCorrectFeed = true;
        for (Comment comment : parentCommentList) {
            if (!comment.isParent()) isCorrectParent = false;
            if (!comment.getFeed().getId().equals(dummyFeed.getId())) isCorrectFeed = false;
        }

        // then
        assertThat(parentCommentList.getSize()).isEqualTo(limit);
        assertThat(isCorrectParent).isTrue();
        assertThat(isCorrectFeed).isTrue();
    }
}
