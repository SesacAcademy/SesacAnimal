package com.project.animal.review.service;

import com.project.animal.global.common.dto.ImageListDto;
import com.project.animal.global.common.provider.MinioServiceProvider;
import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.exception.ImageUpdateException;
import com.project.animal.review.repository.ReviewImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class ReviewImageService {

    private final MinioServiceProvider minioServiceProvider;

    private final ReviewImageRepository reviewImageRepository;

        public void saveImg(List<MultipartFile> imageFiles, ReviewPost reviewPost){
            List<String> urls = uploadMinio(imageFiles);
            for (String url : urls) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .url(url)
                        .reviewPost(reviewPost)
                        .isActive(1)
                        .build();
                reviewImageRepository.save(reviewImage);
            }
        }
        private List<String> uploadMinio(List<MultipartFile> imageFiles){
            ImageListDto imageListDto = new ImageListDto(imageFiles);
            return minioServiceProvider.insertImageMinio(imageListDto, minioServiceProvider.REVIEW);
        }

    public void imageChangeStatus(List<Long> imageIds) {
        List<ReviewImage> list = reviewImageRepository.findAllByIds(imageIds);
        if (list.isEmpty()){
            return;
        }
        int count = 0;
        int listSize = list.size();
        for (ReviewImage image:list) {
            image.changeStatus();
            count++;
        }
        if (!(count == listSize)){
            throw new ImageUpdateException("상태를 변경하려는 이미지 갯수와 변경된 이미지 갯수가 일치하지 않습니다." +
                    " 변경하려는 이미지 갯수:" + listSize +
                    " 변경한 이미지 갯수: " + listSize);
        }
    }
}
