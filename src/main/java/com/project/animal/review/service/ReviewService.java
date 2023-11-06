package com.project.animal.review.service;

import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewPostDto;
import com.project.animal.review.dto.ReadAllGeneric;
import com.project.animal.review.dto.ReviewPostAllDto;
import com.project.animal.review.repository.ReviewImageRepository;
import com.project.animal.review.repository.ReviewRepository;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private MinioClient minioClient;
    private final String bucketName = "review";


    public void createReviewPost(CreateReviewPostDto createReviewPostDto, MultipartFile file, Member member) {
        ReviewPost reviewPost = new ReviewPost(createReviewPostDto, member);
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
                    is.close();
                    return true;
                }
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }
    // pagable 객체 작성
    //entity 받은 거 리턴은 dto로 필요한 것만
    //제너릭 타입에 카운트랑 dto list  형태로 리턴
    public ReadAllGeneric readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewPost> postList = reviewRepository.findAllByOrderByCreatedAtDescWithMember(pageable);
        return entityToDtoByReadAll(postList);
    }

    private ReadAllGeneric entityToDtoByReadAll(Page<ReviewPost> entity) {
        int count =  entity.getTotalPages();
        int pageNum = entity.getNumber();
        List<ReviewPostAllDto> dtoList = entity.getContent()
                .stream()
                .map(reviewPost -> new ReviewPostAllDto(reviewPost))
                .collect(Collectors.toList());
        return ReadAllGeneric.builder()
                .list(dtoList)
                .count(count)
                .pageNumber(pageNum)
                .build();
    }



}
