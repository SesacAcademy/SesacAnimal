package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Log4j2
@NoArgsConstructor
public class ReviewIndexResponse {

    private String title;

    private String content;

    private int viewCount;

    private String url;

    private int postLikeCount;

    public ReviewIndexResponse(ReviewPost reviewPost){
        this.title =  reviewPost.getTitle();
        this.content = reviewPost.getContent();
        this.viewCount = reviewPost.getViewCount();
        this.url = getActiveImage(reviewPost.getReviewImages());
        this.postLikeCount = reviewPost.getReviewPostLikes().size();

    }
    private String getActiveImage(List<ReviewImage> images) {
        return images.stream()
                .filter(image -> image.getIsActiveToDto() == 1)
                .findFirst()
                .map(ReviewImage::getUrl)
                .orElse(null);
    }
}
