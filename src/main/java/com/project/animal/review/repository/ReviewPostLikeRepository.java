package com.project.animal.review.repository;

import com.project.animal.review.domain.ReviewPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewPostLikeRepository extends JpaRepository<ReviewPostLike, Long> {
    Optional<ReviewPostLike> findByMemberIdAndReviewPostId(Long memberId, Long reviewPostId);
}
