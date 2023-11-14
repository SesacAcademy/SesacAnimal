package com.project.animal.review.repository;

import com.project.animal.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    @Query("SELECT ri FROM ReviewImage ri WHERE ri.id IN :ids")
    List<ReviewImage> findAllByIds(@Param("ids") List<Long> ids);


}
