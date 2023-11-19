package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Log4j2
@NoArgsConstructor
public class ReviewPostAllDto {

    private Long reviewPostId;

    private String title;

    private String content;

    private int viewCount;

    private LocalDateTime updatedAt;

    private String memberName;

    private String url;
    private int commentCount;

    public ReviewPostAllDto(ReviewPost reviewPost){
        this.reviewPostId = reviewPost.getId();
        this.title = reviewPost.getTitle();
        this.content = reviewPost.getContent();
        this.viewCount = reviewPost.getViewCount();
        this.updatedAt = reviewPost.getUpdatedAt();
        this.memberName = reviewPost.getMember().getName();
        this.url = getActiveImage(reviewPost.getReviewImages());
        this.commentCount = reviewPost.getComments().size();
        this.viewCount = reviewPost.getViewCount();
    }

    private String setList(List<ReviewImage> images) {

        if (images.isEmpty()){
            return null;
        }
        for (ReviewImage reviewImage:images) {
            if (reviewImage.getIsActiveToDto()==1){
                return reviewImage.getUrl();
            }
        } return null;
    }
    private String getActiveImage(List<ReviewImage> images) {
        return images.stream()
                .filter(image -> image.getIsActiveToDto() == 1)
                .findFirst()
                .map(ReviewImage::getUrl)
                .orElse(null);
    }
}

