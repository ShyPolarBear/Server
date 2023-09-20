package com.shy_polarbear.server.domain.notification.model;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    NEW_FEED_COMMENT(NotificationReceiverType.AUTHOR, Feed.class),
    NEW_COMMENT_CHILD_COMMENT(NotificationReceiverType.AUTHOR, Feed.class);
//    LIMIT(NotificationReceiverType.COMMON, User.class);

    private final NotificationReceiverType notificationReceiverType;
    private final Class<? extends BaseEntity> redirectTargetClass;
}
