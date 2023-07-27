package com.shy_polarbear.server.domain.feed.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedLike is a Querydsl query type for FeedLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedLike extends EntityPathBase<FeedLike> {

    private static final long serialVersionUID = -904040729L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedLike feedLike = new QFeedLike("feedLike");

    public final com.shy_polarbear.server.global.common.model.QBaseEntity _super = new com.shy_polarbear.server.global.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QFeed feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.shy_polarbear.server.domain.user.model.QUser user;

    public QFeedLike(String variable) {
        this(FeedLike.class, forVariable(variable), INITS);
    }

    public QFeedLike(Path<? extends FeedLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedLike(PathMetadata metadata, PathInits inits) {
        this(FeedLike.class, metadata, inits);
    }

    public QFeedLike(Class<? extends FeedLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new QFeed(forProperty("feed"), inits.get("feed")) : null;
        this.user = inits.isInitialized("user") ? new com.shy_polarbear.server.domain.user.model.QUser(forProperty("user")) : null;
    }

}

