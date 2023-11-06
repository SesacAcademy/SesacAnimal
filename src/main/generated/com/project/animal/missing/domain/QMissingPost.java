package com.project.animal.missing.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMissingPost is a Querydsl query type for MissingPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissingPost extends EntityPathBase<MissingPost> {

    private static final long serialVersionUID = 1179330516L;

    public static final QMissingPost missingPost = new QMissingPost("missingPost");

    public final StringPath animalType = createString("animalType");

    public final StringPath color = createString("color");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> missingId = createNumber("missingId", Long.class);

    public final StringPath missingPlace = createString("missingPlace");

    public final NumberPath<Integer> missingStatus = createNumber("missingStatus", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> missingTime = createDateTime("missingTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> reward = createNumber("reward", Integer.class);

    public final StringPath specifics = createString("specifics");

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QMissingPost(String variable) {
        super(MissingPost.class, forVariable(variable));
    }

    public QMissingPost(Path<? extends MissingPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMissingPost(PathMetadata metadata) {
        super(MissingPost.class, metadata);
    }

}

