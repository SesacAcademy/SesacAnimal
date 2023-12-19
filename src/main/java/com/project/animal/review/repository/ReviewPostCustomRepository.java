package com.project.animal.review.repository;

import com.project.animal.member.domain.QMember;
import com.project.animal.review.domain.ReviewPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

import static com.project.animal.member.domain.QMember.member;
import static com.project.animal.review.domain.QReviewPost.reviewPost;
import static com.project.animal.review.domain.QReviewPostLike.reviewPostLike;


@Repository
@Log4j2
public class ReviewPostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public ReviewPostCustomRepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    // 제목, 작성자, 내용 검색 따른 동적 쿼리 작성
    // 투원 관계 - > 패치조인, 투 매니(컬렉션) 관계 -> 배치 활용
    public Page<ReviewPost> findAllWithMemberAndImageByTypeAndKeyword(String type, String keyword, Pageable pageable) {

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
                .where(builder)
                .orderBy(reviewPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(reviewPost)
                .leftJoin(reviewPost.member, member)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
    // 조회순, 좋아요순에 따른 동적 쿼리 구현
    // 투원 관계 - > 패치조인, 투 매니(컬렉션) 관계 -> 배치 활용
    public Page<ReviewPost> findAllByType(String type, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(reviewPost.isActive.eq(1));

        JPAQuery<ReviewPost> query = jpaQueryFactory
                .selectFrom(reviewPost)
                .leftJoin(reviewPost.member, member).fetchJoin()
                .leftJoin(reviewPost.reviewPostLikes, reviewPostLike)
                .where(builder);

        if ("view".equals(type)) {
            query.orderBy(reviewPost.viewCount.desc());
        } else if ("like".equals(type)) {
            query.groupBy(reviewPost.id)
                    .orderBy(reviewPostLike.count().desc());
        }

        List<ReviewPost> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(reviewPost)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }



    public List<ReviewPost> findPostByLike() {

        return jpaQueryFactory
                .selectFrom(reviewPost)
                .groupBy(reviewPost.id)
                .orderBy(reviewPost.reviewPostLikes.size().desc())
                .limit(3)
                .fetch();
    }
}
