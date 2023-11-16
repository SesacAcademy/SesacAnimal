package com.project.animal.missing.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissingPost is a Querydsl query type for MissingPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissingPost extends EntityPathBase<MissingPost> {

    private static final long serialVersionUID = 1179330516L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissingPost missingPost = new QMissingPost("missingPost");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    public final StringPath animalType = createString("animalType");

    public final StringPath color = createString("color");

    public final ListPath<MissingComment, QMissingComment> comments = this.<MissingComment, QMissingComment>createList("comments", MissingComment.class, QMissingComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final ListPath<MissingPostImage, QMissingPostImage> images = this.<MissingPostImage, QMissingPostImage>createList("images", MissingPostImage.class, QMissingPostImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    public final com.project.animal.member.domain.QMember member;

    public final NumberPath<Long> missingId = createNumber("missingId", Long.class);

    public final StringPath missingPlace = createString("missingPlace");

    public final NumberPath<Integer> missingStatus = createNumber("missingStatus", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> missingTime = createDateTime("missingTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> reward = createNumber("reward", Integer.class);

    public final StringPath specifics = createString("specifics");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QMissingPost(String variable) {
        this(MissingPost.class, forVariable(variable), INITS);
    }

    public QMissingPost(Path<? extends MissingPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissingPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissingPost(PathMetadata metadata, PathInits inits) {
        this(MissingPost.class, metadata, inits);
    }

    public QMissingPost(Class<? extends MissingPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

