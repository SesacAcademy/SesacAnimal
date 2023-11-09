package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ReviewPostAllDto {

    private Long reviewPostId;

    private String title;

    private String content;

    private int viewCount;

    private LocalDateTime updatedAt;

    private String memberName;

    private String url;

    public ReviewPostAllDto(ReviewPost reviewPost){
        this.reviewPostId = reviewPost.getId();
        this.title = reviewPost.getTitle();
        this.content = reviewPost.getContent();
        this.viewCount = reviewPost.getViewCount();
        this.updatedAt = reviewPost.getUpdatedAt();
        this.memberName = reviewPost.getMember().getName();
        this.url = setList(reviewPost.getReviewImages());
    }
    private String setList(List<ReviewImage> images) {
        if (images.size()==0){
            return null;
        }
        ReviewImage reviewImage = images.get(0);
        return reviewImage.getUrl();
    }
}
