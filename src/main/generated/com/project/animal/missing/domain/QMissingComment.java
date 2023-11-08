package com.project.animal.missing.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMissingComment is a Querydsl query type for MissingComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissingComment extends EntityPathBase<MissingComment> {

    private static final long serialVersionUID = 1944625835L;

    public static final QMissingComment missingComment = new QMissingComment("missingComment");

    public final NumberPath<Long> comment_id = createNumber("comment_id", Long.class);

    public final StringPath content = createString("content");

    public final NumberPath<Long> member_id = createNumber("member_id", Long.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final NumberPath<Long> post_id = createNumber("post_id", Long.class);

    public QMissingComment(String variable) {
        super(MissingComment.class, forVariable(variable));
    }

    public QMissingComment(Path<? extends MissingComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMissingComment(PathMetadata metadata) {
        super(MissingComment.class, metadata);
    }

}

