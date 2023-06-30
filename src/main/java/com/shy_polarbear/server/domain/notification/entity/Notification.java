package com.shy_polarbear.server.domain.notification.entity;

import com.shy_polarbear.server.domain.comment.entity.Comment;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notofocation_id")
    private Long id;
    private String title;
    private String content;
    private boolean isRead;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
