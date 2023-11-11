package com.project.animal.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewPost is a Querydsl query type for ReviewPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewPost extends EntityPathBase<ReviewPost> {

    private static final long serialVersionUID = 89169916L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewPost reviewPost = new QReviewPost("reviewPost");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    public final com.project.animal.member.domain.QMember member;

    public final ListPath<ReviewImage, QReviewImage> reviewImages = this.<ReviewImage, QReviewImage>createList("reviewImages", ReviewImage.class, QReviewImage.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QReviewPost(String variable) {
        this(ReviewPost.class, forVariable(variable), INITS);
    }

    public QReviewPost(Path<? extends ReviewPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewPost(PathMetadata metadata, PathInits inits) {
        this(ReviewPost.class, metadata, inits);
    }

    public QReviewPost(Class<? extends ReviewPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
    }

}

