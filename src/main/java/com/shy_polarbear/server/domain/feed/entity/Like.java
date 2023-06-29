package com.shy_polarbear.server.domain.feed.entity;

import com.shy_polarbear.server.domain.comment.entity.Comment;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
