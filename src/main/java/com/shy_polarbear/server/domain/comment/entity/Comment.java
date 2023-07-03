package com.shy_polarbear.server.domain.comment.entity;

import com.shy_polarbear.server.domain.feed.entity.Feed;
import com.shy_polarbear.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
    private String content;
    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Comment(User author, String content, Feed feed) {
        this.author = author;
        this.content = content;
        this.feed = feed;
    }


    public void addLike(CommentLike commentLike) {
        this.commentLikes.add(commentLike);
    }

    public void addChildComment(Comment comment) {
        this.childComments.add(comment);
        comment.assignParent(this);
    }

    private void assignParent(Comment comment) {
        this.parent = comment;
    }

}
