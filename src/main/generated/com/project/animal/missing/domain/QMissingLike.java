package com.project.animal.missing.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissingLike is a Querydsl query type for MissingLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissingLike extends EntityPathBase<MissingLike> {

    private static final long serialVersionUID = 1179205323L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissingLike missingLike = new QMissingLike("missingLike");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.project.animal.member.domain.QMember member;

    public final NumberPath<Long> missingLikeId = createNumber("missingLikeId", Long.class);

    public final QMissingPost post;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMissingLike(String variable) {
        this(MissingLike.class, forVariable(variable), INITS);
    }

    public QMissingLike(Path<? extends MissingLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissingLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissingLike(PathMetadata metadata, PathInits inits) {
        this(MissingLike.class, metadata, inits);
    }

    public QMissingLike(Class<? extends MissingLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new QMissingPost(forProperty("post"), inits.get("post")) : null;
    }

}

