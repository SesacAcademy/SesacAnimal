package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdoption is a Querydsl query type for Adoption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdoption extends EntityPathBase<Adoption> {

    private static final long serialVersionUID = 41187900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdoption adoption = new QAdoption("adoption");

    public final QAdoptionImage adoptionImage;

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath breed = createString("breed");

    public final StringPath center = createString("center");

    public final StringPath content = createString("content");

    public final StringPath gender = createString("gender");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.animal.member.domain.QMember member;

    public final StringPath neutered = createString("neutered");

    public final ComparablePath<Character> status = createComparable("status", Character.class);

    public final StringPath title = createString("title");

    public QAdoption(String variable) {
        this(Adoption.class, forVariable(variable), INITS);
    }

    public QAdoption(Path<? extends Adoption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdoption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdoption(PathMetadata metadata, PathInits inits) {
        this(Adoption.class, metadata, inits);
    }

    public QAdoption(Class<? extends Adoption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adoptionImage = inits.isInitialized("adoptionImage") ? new QAdoptionImage(forProperty("adoptionImage"), inits.get("adoptionImage")) : null;
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

