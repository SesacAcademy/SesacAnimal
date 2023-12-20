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


    /**
     * 댓글, 대댓글 불러오는 Repository
     *
     * @version 0.1
     * @author 손승범
     * @Param reviewPostId 댓글을 가져오고자 하는 게시글의 아이디
     * 댓글 수 제한이 있다면 limit 추가
     * */
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
