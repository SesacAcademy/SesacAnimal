package com.project.animal.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewImage is a Querydsl query type for ReviewImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewImage extends EntityPathBase<ReviewImage> {

    private static final long serialVersionUID = -1537241729L;

    public static final QReviewImage reviewImage = new QReviewImage("reviewImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> reviewPostId = createNumber("reviewPostId", Long.class);

    public final StringPath url = createString("url");

    public QReviewImage(String variable) {
        super(ReviewImage.class, forVariable(variable));
    }

    public QReviewImage(Path<? extends ReviewImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewImage(PathMetadata metadata) {
        super(ReviewImage.class, metadata);
    }

}

