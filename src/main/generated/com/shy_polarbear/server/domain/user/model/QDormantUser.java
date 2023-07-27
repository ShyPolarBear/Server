package com.shy_polarbear.server.domain.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDormantUser is a Querydsl query type for DormantUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDormantUser extends EntityPathBase<DormantUser> {

    private static final long serialVersionUID = 403646119L;

    public static final QDormantUser dormantUser = new QDormantUser("dormantUser");

    public final com.shy_polarbear.server.global.common.model.QBaseEntity _super = new com.shy_polarbear.server.global.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBlackListUser = createBoolean("isBlackListUser");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath nickName = createString("nickName");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profileImage = createString("profileImage");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final EnumPath<UserRole> userRole = createEnum("userRole", UserRole.class);

    public final EnumPath<UserStatus> userStatus = createEnum("userStatus", UserStatus.class);

    public QDormantUser(String variable) {
        super(DormantUser.class, forVariable(variable));
    }

    public QDormantUser(Path<? extends DormantUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDormantUser(PathMetadata metadata) {
        super(DormantUser.class, metadata);
    }

}

