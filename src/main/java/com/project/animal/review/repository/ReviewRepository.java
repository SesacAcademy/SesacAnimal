package com.project.animal.review.repository;

import com.project.animal.review.domain.ReviewPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewPost, Long> {
    @Query("SELECT p FROM ReviewPost p JOIN FETCH p.member")
    Page<ReviewPost> findAllByOrderByCreatedAtDescWithMember(Pageable pageable);

}
