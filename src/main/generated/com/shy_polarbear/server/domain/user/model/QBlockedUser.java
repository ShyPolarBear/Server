package com.shy_polarbear.server.domain.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlockedUser is a Querydsl query type for BlockedUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedUser extends EntityPathBase<BlockedUser> {

    private static final long serialVersionUID = 1183947506L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlockedUser blockedUser1 = new QBlockedUser("blockedUser1");

    public final QUser blockedUser;

    public final QUser blockingUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBlockedUser(String variable) {
        this(BlockedUser.class, forVariable(variable), INITS);
    }

    public QBlockedUser(Path<? extends BlockedUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlockedUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlockedUser(PathMetadata metadata, PathInits inits) {
        this(BlockedUser.class, metadata, inits);
    }

    public QBlockedUser(Class<? extends BlockedUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.blockedUser = inits.isInitialized("blockedUser") ? new QUser(forProperty("blockedUser")) : null;
        this.blockingUser = inits.isInitialized("blockingUser") ? new QUser(forProperty("blockingUser")) : null;
    }

}

