package com.project.animal.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewPostLike is a Querydsl query type for ReviewPostLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewPostLike extends EntityPathBase<ReviewPostLike> {

    private static final long serialVersionUID = -1410570829L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewPostLike reviewPostLike = new QReviewPostLike("reviewPostLike");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.animal.member.domain.QMember member;

    public final QReviewPost reviewPost;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewPostLike(String variable) {
        this(ReviewPostLike.class, forVariable(variable), INITS);
    }

    public QReviewPostLike(Path<? extends ReviewPostLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewPostLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewPostLike(PathMetadata metadata, PathInits inits) {
        this(ReviewPostLike.class, metadata, inits);
    }

    public QReviewPostLike(Class<? extends ReviewPostLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
        this.reviewPost = inits.isInitialized("reviewPost") ? new QReviewPost(forProperty("reviewPost"), inits.get("reviewPost")) : null;
    }

}

