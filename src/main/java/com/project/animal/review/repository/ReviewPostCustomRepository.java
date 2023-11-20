package com.project.animal.review.repository;

import com.project.animal.member.domain.QMember;
import com.project.animal.review.domain.QReviewImage;
import com.project.animal.review.domain.QReviewPost;
import com.project.animal.review.domain.ReviewPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewPostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public ReviewPostCustomRepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }
    public Page<ReviewPost> findAllWithMemberAndImageByTypeAndKeyword(String type, String keyword, Pageable pageable) {
        QReviewPost reviewPost = QReviewPost.reviewPost;
        QMember member = QMember.member;
        QReviewImage reviewImage = QReviewImage.reviewImage;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(reviewPost.isActive.eq(1));

        if(type != null && keyword != null) {
            switch(type) {
                case "author":
                    builder.and(member.nickname.eq(keyword));
                    break;
                case "title":
                    builder.and(reviewPost.title.eq(keyword));
                    break;
                case "content":
                    builder.and(reviewPost.content.contains(keyword));
                    break;
            }
        }

        List<ReviewPost> content = jpaQueryFactory
                .selectFrom(reviewPost)
                .leftJoin(reviewPost.member, member).fetchJoin()
                .leftJoin(reviewPost.reviewImages, reviewImage).fetchJoin()
                .where(builder)
                .orderBy(reviewPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(reviewPost)
                .leftJoin(reviewPost.member, member)
                .leftJoin(reviewPost.reviewImages, reviewImage)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}
