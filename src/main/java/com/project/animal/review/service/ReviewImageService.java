package com.project.animal.review.service;

import com.project.animal.global.common.dto.ImageListDto;
import com.project.animal.global.common.minioserviceprovider.ImageUploadMinio;
import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.repository.ReviewImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class ReviewImageService {

    private final ImageUploadMinio imageUploadMinio;

    private final ReviewImageRepository reviewImageRepository;

        public void saveImg(ImageListDto images, ReviewPost reviewPost){
            List<String> urls = imageUploadMinio.insertImageMinio(images,imageUploadMinio.REVIEW);
            for (String url : urls) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .url(url)
                        .reviewPost(reviewPost)
                        .build();
                reviewImageRepository.save(reviewImage);
            }
        }
    }
