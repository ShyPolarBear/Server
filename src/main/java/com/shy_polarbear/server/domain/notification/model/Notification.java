package com.shy_polarbear.server.domain.notification.model;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.model.BaseEntity;
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
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String title;
    @Column(nullable = false, updatable = false)
    private String content;
    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private NotificationType notificationType;

    @Column(nullable = false, updatable = false)
    private Long redirectTargetId;


    @Builder
    private Notification(String title, String content, User receiver, NotificationType notificationType, Long redirectTargetId) {
        this.title = title;
        this.content = content;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.redirectTargetId = redirectTargetId;
    }

    public static Notification createNotification(User receiver, String title, String content, NotificationType notificationType, Long redirectTargetId) {
        return Notification.builder()
                .receiver(receiver)
                .title(title)
                .content(content)
                .notificationType(notificationType)
                .redirectTargetId(redirectTargetId)
                .build();
    }

    public void read() {
        this.isRead = true;
    }
}
