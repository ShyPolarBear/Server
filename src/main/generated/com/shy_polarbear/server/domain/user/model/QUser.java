package com.shy_polarbear.server.domain.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 754496784L;

    public static final QUser user = new QUser("user");

    public final com.shy_polarbear.server.global.common.model.QBaseEntity _super = new com.shy_polarbear.server.global.common.model.QBaseEntity(this);

    public final ListPath<BlockedUser, QBlockedUser> blockedUsers = this.<BlockedUser, QBlockedUser>createList("blockedUsers", BlockedUser.class, QBlockedUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBlackListUser = createBoolean("isBlackListUser");

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath nickName = createString("nickName");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.shy_polarbear.server.domain.point.model.Point, com.shy_polarbear.server.domain.point.model.QPoint> points = this.<com.shy_polarbear.server.domain.point.model.Point, com.shy_polarbear.server.domain.point.model.QPoint>createList("points", com.shy_polarbear.server.domain.point.model.Point.class, com.shy_polarbear.server.domain.point.model.QPoint.class, PathInits.DIRECT2);

    public final StringPath profileImage = createString("profileImage");

    public final StringPath provider = createString("provider");

    public final StringPath providerId = createString("providerId");

    public final EnumPath<UserRole> role = createEnum("role", UserRole.class);

    public final ListPath<com.shy_polarbear.server.domain.quiz.model.UserQuiz, com.shy_polarbear.server.domain.quiz.model.QUserQuiz> userQuiz = this.<com.shy_polarbear.server.domain.quiz.model.UserQuiz, com.shy_polarbear.server.domain.quiz.model.QUserQuiz>createList("userQuiz", com.shy_polarbear.server.domain.quiz.model.UserQuiz.class, com.shy_polarbear.server.domain.quiz.model.QUserQuiz.class, PathInits.DIRECT2);

    public final EnumPath<UserStatus> userStatus = createEnum("userStatus", UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

