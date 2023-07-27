package com.shy_polarbear.server.domain.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdrawnUser is a Querydsl query type for WithdrawnUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdrawnUser extends EntityPathBase<WithdrawnUser> {

    private static final long serialVersionUID = -474179350L;

    public static final QWithdrawnUser withdrawnUser = new QWithdrawnUser("withdrawnUser");

    public final com.shy_polarbear.server.global.common.model.QBaseEntity _super = new com.shy_polarbear.server.global.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QWithdrawnUser(String variable) {
        super(WithdrawnUser.class, forVariable(variable));
    }

    public QWithdrawnUser(Path<? extends WithdrawnUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdrawnUser(PathMetadata metadata) {
        super(WithdrawnUser.class, metadata);
    }

}

