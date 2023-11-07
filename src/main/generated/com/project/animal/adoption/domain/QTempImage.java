package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTempImage is a Querydsl query type for TempImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTempImage extends EntityPathBase<TempImage> {

    private static final long serialVersionUID = 1223125635L;

    public static final QTempImage tempImage = new QTempImage("tempImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public QTempImage(String variable) {
        super(TempImage.class, forVariable(variable));
    }

    public QTempImage(Path<? extends TempImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTempImage(PathMetadata metadata) {
        super(TempImage.class, metadata);
    }

}

