package com.project.animal.review.repository;

import com.project.animal.member.domain.QMember;
import com.project.animal.review.domain.QReviewComment;
import com.project.animal.review.domain.ReviewComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewCommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public ReviewCommentCustomRepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    // 게시글의 댓글 전체 가져오기
    // 게시글 단일 조회 시에 연관관계 댓글 모두 조회
    public List<ReviewComment> findAllByPostId(Long reviewPostId){
        QReviewComment reviewComment = QReviewComment.reviewComment;
        QMember member = QMember.member;
        return jpaQueryFactory.selectFrom(reviewComment)
                .leftJoin(reviewComment.member)
                .fetchJoin()
                .leftJoin(reviewComment.parentComment)
                .fetchJoin()
                .where(reviewComment.reviewPost.id.eq(reviewPostId))
                .orderBy(reviewComment.parentComment.id.asc().nullsFirst(), reviewComment.createdAt.desc())
                .fetch();
    }
}
