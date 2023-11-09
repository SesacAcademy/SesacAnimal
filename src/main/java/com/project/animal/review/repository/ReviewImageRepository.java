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
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO temp_images (image_id, url) SELECT image_id, url FROM images WHERE image_id IN :imageIds", nativeQuery = true)
    void copyImagesToTemporaryTable(@Param("imageIds") List<Long> imageIds);
}
