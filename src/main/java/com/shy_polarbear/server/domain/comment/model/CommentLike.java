package com.shy_polarbear.server.domain.comment.model;

<<<<<<< HEAD:src/main/java/com/shy_polarbear/server/domain/comment/model/CommentLike.java
import com.shy_polarbear.server.domain.user.model.User;
=======
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.global.common.BaseEntity;
>>>>>>> eb05871b6fd8e3daa86135174cfecc35d7ef4c2c:src/main/java/com/shy_polarbear/server/domain/comment/entity/CommentLike.java
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_like_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public static CommentLike createCommentLike(Comment comment, User user) {
        CommentLike commentLike = CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
        comment.addLike(commentLike);
        return commentLike;
    }

}
