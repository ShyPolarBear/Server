package com.shy_polarbear.server.domain.prize.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPrize is a Querydsl query type for Prize
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPrize extends EntityPathBase<Prize> {

    private static final long serialVersionUID = -1580798642L;

    public static final QPrize prize = new QPrize("prize");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath prizeImage = createString("prizeImage");

    public final EnumPath<PrizeStatus> prizeStatus = createEnum("prizeStatus", PrizeStatus.class);

    public QPrize(String variable) {
        super(Prize.class, forVariable(variable));
    }

    public QPrize(Path<? extends Prize> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPrize(PathMetadata metadata) {
        super(Prize.class, metadata);
    }

}

