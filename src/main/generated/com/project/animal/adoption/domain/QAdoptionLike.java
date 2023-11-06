package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdoptionLike is a Querydsl query type for AdoptionLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdoptionLike extends EntityPathBase<AdoptionLike> {

    private static final long serialVersionUID = 1662590963L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdoptionLike adoptionLike = new QAdoptionLike("adoptionLike");

    public final QAdoption adoption;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.animal.member.domain.QMember member;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QAdoptionLike(String variable) {
        this(AdoptionLike.class, forVariable(variable), INITS);
    }

    public QAdoptionLike(Path<? extends AdoptionLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdoptionLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdoptionLike(PathMetadata metadata, PathInits inits) {
        this(AdoptionLike.class, metadata, inits);
    }

    public QAdoptionLike(Class<? extends AdoptionLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adoption = inits.isInitialized("adoption") ? new QAdoption(forProperty("adoption"), inits.get("adoption")) : null;
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

