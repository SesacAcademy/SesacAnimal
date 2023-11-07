package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdoptionComment is a Querydsl query type for AdoptionComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdoptionComment extends EntityPathBase<AdoptionComment> {

    private static final long serialVersionUID = 1460883587L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdoptionComment adoptionComment = new QAdoptionComment("adoptionComment");

    public final QAdoption adoption;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> group_num = createNumber("group_num", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.animal.member.domain.QMember member;

    public final NumberPath<Integer> tab = createNumber("tab", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAdoptionComment(String variable) {
        this(AdoptionComment.class, forVariable(variable), INITS);
    }

    public QAdoptionComment(Path<? extends AdoptionComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdoptionComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdoptionComment(PathMetadata metadata, PathInits inits) {
        this(AdoptionComment.class, metadata, inits);
    }

    public QAdoptionComment(Class<? extends AdoptionComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adoption = inits.isInitialized("adoption") ? new QAdoption(forProperty("adoption"), inits.get("adoption")) : null;
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

