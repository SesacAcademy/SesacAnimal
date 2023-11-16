package com.project.animal.missing.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissingComment is a Querydsl query type for MissingComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissingComment extends EntityPathBase<MissingComment> {

    private static final long serialVersionUID = 1944625835L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissingComment missingComment = new QMissingComment("missingComment");

    public final NumberPath<Long> comment_id = createNumber("comment_id", Long.class);

    public final ListPath<MissingComment, QMissingComment> comments = this.<MissingComment, QMissingComment>createList("comments", MissingComment.class, QMissingComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final com.project.animal.member.domain.QMember member;

    public final QMissingPost missingPost;

    public final QMissingComment parentComment;

    public QMissingComment(String variable) {
        this(MissingComment.class, forVariable(variable), INITS);
    }

    public QMissingComment(Path<? extends MissingComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissingComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissingComment(PathMetadata metadata, PathInits inits) {
        this(MissingComment.class, metadata, inits);
    }

    public QMissingComment(Class<? extends MissingComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.project.animal.member.domain.QMember(forProperty("member")) : null;
        this.missingPost = inits.isInitialized("missingPost") ? new QMissingPost(forProperty("missingPost"), inits.get("missingPost")) : null;
        this.parentComment = inits.isInitialized("parentComment") ? new QMissingComment(forProperty("parentComment"), inits.get("parentComment")) : null;
    }

}

