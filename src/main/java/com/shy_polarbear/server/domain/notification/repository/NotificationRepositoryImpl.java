package com.shy_polarbear.server.domain.notification.repository;

import static com.shy_polarbear.server.domain.notification.model.QNotification.notification;
import static com.shy_polarbear.server.domain.user.model.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shy_polarbear.server.domain.notification.model.Notification;
import com.shy_polarbear.server.domain.notification.model.QNotification;
import com.shy_polarbear.server.domain.user.model.QUser;
import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findAllByReceiverId(Long userId){
        JPAQuery<Notification> query = queryFactory.selectFrom(notification)
                .where(notification.receiver.id.eq(userId))
                .orderBy(notification.id.desc())
                .limit(BusinessLogicConstants.RECENT_NOTIFICATION_LIMIT);

        return query.fetch();
    };

    @Override
    public Optional<Notification> findByIdAndReceiverId(Long notificationId, Long receiverId) {
        JPAQuery<Notification> query = queryFactory.selectFrom(notification)
                .where(notification.id.eq(notificationId)
                        .and(notification.receiver.id.eq(receiverId)));
        return Optional.ofNullable(query.fetchOne());
    }

}
