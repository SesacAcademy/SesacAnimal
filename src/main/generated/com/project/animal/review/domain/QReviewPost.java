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

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isActive = createNumber("isActive", Integer.class);

    public final com.project.animal.member.domain.QMember member;

    public final StringPath nickname = createString("nickname");

    public final QReviewImage reviewImage;

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

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
        this.reviewImage = inits.isInitialized("reviewImage") ? new QReviewImage(forProperty("reviewImage")) : null;
    }

}

