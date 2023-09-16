package com.shy_polarbear.server.domain.comment.model;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, length = BusinessLogicConstants.COMMENT_CONTENT_MAX_LENGTH)
    private String content;

    @Column(nullable = false)
    private Boolean visibility = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Comment> childComments = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private final List<CommentLike> commentLikes = new ArrayList<>();


    @Builder
    private Comment(User author, String content, Feed feed) {
        this.author = author;
        this.content = content;
        this.feed = feed;
    }

    public static Comment createComment(User author, String content, Feed feed) {
        return Comment.builder()
                .author(author)
                .feed(feed)
                .content(content)
                .build();
    }

    public static Comment createChildComment(User author, String content, Feed feed, Comment parent) {
        Comment childComment = Comment.builder()
                .author(author)
                .feed(feed)
                .content(content)
                .build();
        parent.addChildComment(childComment);
        return childComment;
    }


    public void update(String content) {
        this.content = content;
    }

    public void softDelete() {  // 논리적 삭제(소프트 딜리트와 유사)
        this.visibility = false;
    }


    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }

    public void addChildComment(Comment comment) {
        this.childComments.add(comment);
        comment.assignParent(this);
    }

    private void assignParent(Comment comment) {
        this.parent = comment;
    }

    public boolean isParent() {
        return Objects.isNull(this.parent);
    }

    public boolean isChild() {
        return Objects.nonNull(this.parent);
    }

    public void addLike(CommentLike commentLike) {
        this.commentLikes.add(commentLike);
    }

    public boolean isLike(User user) {
        return commentLikes.stream()
                .anyMatch(commentLike -> commentLike.isAuthor(user));
    }

}
