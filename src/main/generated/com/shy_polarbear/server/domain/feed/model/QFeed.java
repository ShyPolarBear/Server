package com.shy_polarbear.server.domain.feed.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeed is a Querydsl query type for Feed
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeed extends EntityPathBase<Feed> {

    private static final long serialVersionUID = 591291184L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeed feed = new QFeed("feed");

    public final com.shy_polarbear.server.global.common.model.QBaseEntity _super = new com.shy_polarbear.server.global.common.model.QBaseEntity(this);

    public final com.shy_polarbear.server.domain.user.model.QUser author;

    public final ListPath<com.shy_polarbear.server.domain.comment.model.Comment, com.shy_polarbear.server.domain.comment.model.QComment> comments = this.<com.shy_polarbear.server.domain.comment.model.Comment, com.shy_polarbear.server.domain.comment.model.QComment>createList("comments", com.shy_polarbear.server.domain.comment.model.Comment.class, com.shy_polarbear.server.domain.comment.model.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<String, StringPath> feedImages = this.<String, StringPath>createList("feedImages", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<FeedLike, QFeedLike> feedLikes = this.<FeedLike, QFeedLike>createList("feedLikes", FeedLike.class, QFeedLike.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath title = createString("title");

    public QFeed(String variable) {
        this(Feed.class, forVariable(variable), INITS);
    }

    public QFeed(Path<? extends Feed> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeed(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeed(PathMetadata metadata, PathInits inits) {
        this(Feed.class, metadata, inits);
    }

    public QFeed(Class<? extends Feed> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.shy_polarbear.server.domain.user.model.QUser(forProperty("author")) : null;
    }

}

