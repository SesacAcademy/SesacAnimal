package com.project.animal.missing.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissingPostImage is a Querydsl query type for MissingPostImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissingPostImage extends EntityPathBase<MissingPostImage> {

    private static final long serialVersionUID = -342862361L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissingPostImage missingPostImage = new QMissingPostImage("missingPostImage");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> imageId = createNumber("imageId", Long.class);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    public final QMissingPost missingPost;

    public final StringPath path = createString("path");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMissingPostImage(String variable) {
        this(MissingPostImage.class, forVariable(variable), INITS);
    }

    public QMissingPostImage(Path<? extends MissingPostImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissingPostImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissingPostImage(PathMetadata metadata, PathInits inits) {
        this(MissingPostImage.class, metadata, inits);
    }

    public QMissingPostImage(Class<? extends MissingPostImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.missingPost = inits.isInitialized("missingPost") ? new QMissingPost(forProperty("missingPost")) : null;
    }

}

