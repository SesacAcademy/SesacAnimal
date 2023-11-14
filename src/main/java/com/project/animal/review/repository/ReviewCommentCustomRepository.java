package com.project.animal.review.repository;

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
    public List<ReviewComment> findAllByPost(Long reviewPostId){
        QReviewComment reviewComment = QReviewComment.reviewComment;
        return jpaQueryFactory.selectFrom(reviewComment)
                .leftJoin(reviewComment.parentComment)
                .fetchJoin()
                .where(reviewComment.reviewPost.id.eq(reviewPostId))
                .orderBy(reviewComment.parentComment.id.asc().nullsFirst(), reviewComment.createdAt.asc())
                .fetch();
    }
}
