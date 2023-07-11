package com.shy_polarbear.server.domain.notification.model;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Builder
    private Notification(String title, String content, User user, NotificationType notificationType, Comment comment) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.comment = comment;
        this.notificationType = notificationType;
    }

    //알림타입이 댓글/대댓글일 경우
    public static Notification createCommentNotification(String title, String content, User user, NotificationType notificationType, Comment comment) {
        return Notification.builder()
                .title(title)
                .content(content)
                .user(user)
                .notificationType(notificationType)
                .comment(comment)
                .build();
    }

    //알림 타입이 제한일 경우
    public static Notification createLimitNotification(String title, String content, User user) {
        return Notification.builder()
                .title(title)
                .content(content)
                .user(user)
                .notificationType(NotificationType.LIMIT)
                .build();
    }
}
