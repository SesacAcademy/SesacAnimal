package com.project.animal.adoption.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdoptionImage is a Querydsl query type for AdoptionImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdoptionImage extends EntityPathBase<AdoptionImage> {

    private static final long serialVersionUID = -1948545L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdoptionImage adoptionImage = new QAdoptionImage("adoptionImage");

    public final QAdoption adoption;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath path = createString("path");

    public QAdoptionImage(String variable) {
        this(AdoptionImage.class, forVariable(variable), INITS);
    }

    public QAdoptionImage(Path<? extends AdoptionImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdoptionImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdoptionImage(PathMetadata metadata, PathInits inits) {
        this(AdoptionImage.class, metadata, inits);
    }

    public QAdoptionImage(Class<? extends AdoptionImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adoption = inits.isInitialized("adoption") ? new QAdoption(forProperty("adoption"), inits.get("adoption")) : null;
    }

}

