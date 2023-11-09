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

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    public final ListPath<AdoptionImage, QAdoptionImage> adoptionImages = this.<AdoptionImage, QAdoptionImage>createList("adoptionImages", AdoptionImage.class, QAdoptionImage.class, PathInits.DIRECT2);

    public final StringPath age = createString("age");

    public final StringPath breed = createString("breed");

    public final StringPath center = createString("center");

    public final StringPath centerAddr = createString("centerAddr");

    public final StringPath centerName = createString("centerName");

    public final StringPath color = createString("color");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath desertionNo = createString("desertionNo");

    public final StringPath gender = createString("gender");

    public final StringPath happenPlace = createString("happenPlace");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isActive = createString("isActive");

    public final com.project.animal.member.domain.QMember member;

    public final StringPath neutered = createString("neutered");

    public final StringPath noticeEdt = createString("noticeEdt");

    public final StringPath noticeNo = createString("noticeNo");

    public final StringPath noticeSdt = createString("noticeSdt");

    public final StringPath specialMark = createString("specialMark");

    public final StringPath status = createString("status");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

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
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

