package com.shy_polarbear.server.domain.comment.model;

import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.user.model.User;

import com.shy_polarbear.server.global.common.model.BaseEntity;
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

    @Enumerated(EnumType.STRING)
    private CommentInfo commentInfo;

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
    private Comment(User author, String content, List<CommentReport> commentReports,
                   Comment parent, Feed feed, CommentInfo commentInfo) {
        this.author = author;
        this.content = content;
        this.commentReports = commentReports;
        this.parent = parent;
        this.feed = feed;
        this.commentStatus = CommentStatus.ENGAGED;
        this.commentInfo = commentInfo;
    }

    public static Comment createComment(User author, String content, Feed feed, CommentInfo commentInfo) {
        return Comment.builder()
                .author(author)
                .feed(feed)
                .content(content)
                .commentInfo(commentInfo)
                .build();
    }

    public static Comment createChildComment(User author, String content, Feed feed, Comment parent, CommentInfo commentInfo) {
        Comment childComment = Comment.builder()
                .author(author)
                .feed(feed)
                .content(content)
                .commentInfo(CommentInfo.CHILD_COMMENT)
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
        return commentLikes;
    }

    public Long getLikesCount() {
        return (long) commentLikes.size();
    }

    public Boolean isAuthor(User currentUser) {
        return author.equals(currentUser);
    }

    public Boolean isLike(User currentUser){
        return commentLikes.stream().anyMatch(like -> like.getUser().equals(currentUser));
    }

    public Boolean isDeleted(){
        return commentStatus == CommentStatus.DELETED;
    }

    private void assignParent(Comment comment) {
        this.parent = comment;
    }
}
