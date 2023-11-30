package com.project.animal.review.repository;

import com.project.animal.review.domain.ReviewPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ManyToOne;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewPost, Long> {
    @Query("SELECT p FROM ReviewPost p JOIN FETCH p.member m LEFT JOIN p.reviewImages i WHERE p.id = :id AND p.isActive = 1")
    Optional<ReviewPost> findByIdWithMemberAndImage(@Param("id") Long id);

    @Query(value = "SELECT p FROM ReviewPost p JOIN FETCH p.member m LEFT JOIN FETCH p.reviewImages i WHERE p.isActive = 1",
            countQuery = "SELECT count(p.id) FROM ReviewPost p WHERE p.isActive = 1")
    Page<ReviewPost> findAllPrevious(Pageable pageable);
    @Query(value = "SELECT p FROM ReviewPost p JOIN FETCH p.member m WHERE p.isActive = 1",
            countQuery = "SELECT count(p.id) FROM ReviewPost p WHERE p.isActive = 1")
    Page<ReviewPost> findAll(Pageable pageable);
    Optional<ReviewPost> findById(Long reviewPostId);
    @Query(value = "SELECT p FROM ReviewPost p JOIN FETCH p.member m LEFT JOIN p.reviewImages i WHERE m.nickname = :nickname AND p.isActive = 1",
            countQuery = "SELECT count(p.id) FROM ReviewPost p JOIN p.member m WHERE m.nickname = :nickname AND p.isActive = 1")
    Page<ReviewPost> findAllWithMemberAndImageByNickname(@Param("nickname") String nickname, Pageable pageable);

}
