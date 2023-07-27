package com.shy_polarbear.server.domain.quiz.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserQuiz is a Querydsl query type for UserQuiz
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserQuiz extends EntityPathBase<UserQuiz> {

    private static final long serialVersionUID = -1974540357L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserQuiz userQuiz = new QUserQuiz("userQuiz");

    public final com.shy_polarbear.server.global.common.model.QBaseEntity _super = new com.shy_polarbear.server.global.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCorrect = createBoolean("isCorrect");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QQuiz quiz;

    public final com.shy_polarbear.server.domain.user.model.QUser user;

    public final StringPath userAnswer = createString("userAnswer");

    public QUserQuiz(String variable) {
        this(UserQuiz.class, forVariable(variable), INITS);
    }

    public QUserQuiz(Path<? extends UserQuiz> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserQuiz(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserQuiz(PathMetadata metadata, PathInits inits) {
        this(UserQuiz.class, metadata, inits);
    }

    public QUserQuiz(Class<? extends UserQuiz> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.quiz = inits.isInitialized("quiz") ? new QQuiz(forProperty("quiz")) : null;
        this.user = inits.isInitialized("user") ? new com.shy_polarbear.server.domain.user.model.QUser(forProperty("user")) : null;
    }

}

