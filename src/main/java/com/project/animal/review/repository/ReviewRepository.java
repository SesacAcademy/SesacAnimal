package com.project.animal.review.repository;

import com.project.animal.review.domain.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewPost, Long> {
}
