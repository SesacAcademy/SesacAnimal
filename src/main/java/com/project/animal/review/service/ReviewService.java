package com.project.animal.review.service;

import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewPostDto;
import com.project.animal.review.repository.ReviewImageRepository;
import com.project.animal.review.repository.ReviewRepository;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@AllArgsConstructor
@Log4j2
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private MinioClient minioClient;
    private final String bucketName = "review";


    public void createReviewPost(CreateReviewPostDto createReviewPostDto, MultipartFile file) {
        ReviewPost reviewPost = new ReviewPost(createReviewPostDto);
        reviewRepository.save(reviewPost);
        Long postId = reviewPost.getId();
        saveImg(file, postId);
    }
    private void saveImg(MultipartFile file, Long postId){
        if (checkImgEmpty(file)){
            String url = getImgUrl(file.getName());
            //이미지 레포지토리 저장
            ReviewImage reviewImage = ReviewImage.builder()
                    .url(url)
                    .reviewPostId(postId)
                    .build();
            reviewImageRepository.save(reviewImage);
        }
    }
    private String getImgUrl(String objectName){
        String ee = "http://infra.shucloud.site:8006/browser/review";
        return ee + "/" + bucketName + "/" +objectName;
    }
    private boolean checkBucket() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            return found;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private Boolean checkImgEmpty(MultipartFile file) {
        try {
            if (checkBucket()) {
                if (!file.isEmpty()) {
                    InputStream is = file.getInputStream();
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket("review")
                                    .object(file.getName())
                                    .stream(is, is.available(), 0)
                                    .build()
                    );
                    return true;
                }
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }



}
