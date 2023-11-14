package com.project.animal.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewComment is a Querydsl query type for ReviewComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewComment extends EntityPathBase<ReviewComment> {

    private static final long serialVersionUID = -782083709L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewComment reviewComment = new QReviewComment("reviewComment");

    public final com.project.animal.global.common.entity.QBaseEntity _super = new com.project.animal.global.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.animal.member.domain.QMember member;

    public final QReviewComment parentComment;

    public final QReviewPost reviewPost;

    public final ListPath<ReviewComment, QReviewComment> subReviewComments = this.<ReviewComment, QReviewComment>createList("subReviewComments", ReviewComment.class, QReviewComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReviewComment(String variable) {
        this(ReviewComment.class, forVariable(variable), INITS);
    }

    public QReviewComment(Path<? extends ReviewComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewComment(PathMetadata metadata, PathInits inits) {
        this(ReviewComment.class, metadata, inits);
    }

    public QReviewComment(Class<? extends ReviewComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
        this.parentComment = inits.isInitialized("parentComment") ? new QReviewComment(forProperty("parentComment"), inits.get("parentComment")) : null;
        this.reviewPost = inits.isInitialized("reviewPost") ? new QReviewPost(forProperty("reviewPost"), inits.get("reviewPost")) : null;
    }

}

