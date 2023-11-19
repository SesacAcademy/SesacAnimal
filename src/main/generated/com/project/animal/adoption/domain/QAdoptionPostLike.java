package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdoptionPostLike is a Querydsl query type for AdoptionPostLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdoptionPostLike extends EntityPathBase<AdoptionPostLike> {

    private static final long serialVersionUID = -598061389L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdoptionPostLike adoptionPostLike = new QAdoptionPostLike("adoptionPostLike");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    public final QAdoption adoption;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.animal.member.domain.QMember member;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAdoptionPostLike(String variable) {
        this(AdoptionPostLike.class, forVariable(variable), INITS);
    }

    public QAdoptionPostLike(Path<? extends AdoptionPostLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdoptionPostLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdoptionPostLike(PathMetadata metadata, PathInits inits) {
        this(AdoptionPostLike.class, metadata, inits);
    }

    public QAdoptionPostLike(Class<? extends AdoptionPostLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adoption = inits.isInitialized("adoption") ? new QAdoption(forProperty("adoption"), inits.get("adoption")) : null;
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

