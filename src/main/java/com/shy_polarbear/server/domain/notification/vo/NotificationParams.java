package com.shy_polarbear.server.domain.notification.vo;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.notification.model.NotificationType;
import com.shy_polarbear.server.domain.user.model.User;
import lombok.Builder;

public record NotificationParams(
        User receiver,
        NotificationType notificationType,
        Long redirectTargetId,
        String title,
        String content
){
    @Builder
    public NotificationParams {}

    public static NotificationParams ofNewFeedComment(
            User feedAuthor,
            Feed feed,
            Comment comment
    ) {
        final String content = """
                %s 글에 댓글이 달렸어요!
                %s""".formatted(feed.getTitle(), comment.getContent());
        return NotificationParams.builder()
                .receiver(feedAuthor)
                .notificationType(NotificationType.NEW_FEED_COMMENT)
                .redirectTargetId(feed.getId())
                .title(feed.getTitle())
                .content(content)
                .build();
    }

    public static NotificationParams ofNewChildComment(
            User parentCommentAuthor,
            Feed feed,
            Comment childComment
    ) {
        final String content = """
                %s 글에 대댓글이 달렸어요!
                %s""".formatted(feed.getTitle(), childComment.getContent());
        return NotificationParams.builder()
                .receiver(parentCommentAuthor)
                .notificationType(NotificationType.NEW_COMMENT_CHILD_COMMENT)
                .redirectTargetId(feed.getId())
                .title(feed.getTitle())
                .content(content)
                .build();
    }
}
