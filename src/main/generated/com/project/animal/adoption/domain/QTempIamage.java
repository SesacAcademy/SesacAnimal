package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTempIamage is a Querydsl query type for TempIamage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTempIamage extends EntityPathBase<TempIamage> {

    private static final long serialVersionUID = -748541342L;

    public static final QTempIamage tempIamage = new QTempIamage("tempIamage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public QTempIamage(String variable) {
        super(TempIamage.class, forVariable(variable));
    }

    public QTempIamage(Path<? extends TempIamage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTempIamage(PathMetadata metadata) {
        super(TempIamage.class, metadata);
    }

}

