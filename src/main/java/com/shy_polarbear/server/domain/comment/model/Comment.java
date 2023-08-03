package com.shy_polarbear.server.domain.comment.model;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;

import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReport> commentReports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();


    @Builder
    public Comment(Long id, User author, String content,
                   List<CommentLike> commentLikes, List<CommentReport> commentReports,
                   Comment parent, List<Comment> childComments, Feed feed) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.commentLikes = commentLikes;
        this.commentReports = commentReports;
        this.parent = parent;
        this.childComments = childComments;
        this.feed = feed;
        this.commentStatus = CommentStatus.ENGAGED;
    }

    public static Comment createComment(User author, String content, Feed feedId) {
        return Comment.builder()
                .author(author)
                .feed(feedId)
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

    public void updateContent(String content){
        this.content = content;
    }

    public void reportComment(){
        this.commentStatus = CommentStatus.REPORTED;
    }

    public void addReport(CommentReport commentReport) {
        this.commentReports.add(commentReport);
    }

    public void addLike(CommentLike commentLike) {
        this.commentLikes.add(commentLike);
    }

    public void addChildComment(Comment comment) {
        this.childComments.add(comment);
        comment.assignParent(this);
    }

    public List<CommentLike> getCommentLikes() {
        return Collections.unmodifiableList(commentLikes);
    }

    public Boolean getIsAuthor(User currentUser) {
        return author != null && author.equals(currentUser);
    }

    public LocalDateTime getCreatedDate() {
        return getCreatedAt(); // BaseEntity의 getCreatedAt() 메서드 사용
    }

    private void assignParent(Comment comment) {
        this.parent = comment;
    }
}
