package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadOneReviewDto {
    private String title;
    private String nickname;
    private LocalDateTime updatedAt;
    private String content;
    private int viewCount;
    private Map<Long,String> urls = new LinkedHashMap<>();
    private Long reviewPostId;
    private Long memberId;
    private int postLikeCount;

    public ReadOneReviewDto(ReviewPost reviewPost) {
        this.title = reviewPost.getTitle();
        this.reviewPostId = reviewPost.getId();
        this.nickname = reviewPost.getMember().getNickname();
        this.memberId = reviewPost.getMember().getId();
        this.updatedAt = reviewPost.getUpdatedAt();
        this.content = reviewPost.getContent();
        this.viewCount = reviewPost.getViewCount();
        this.postLikeCount = reviewPost.getReviewPostLikes().size();
        this.urls = setIdAndUrlAndCheckActive(reviewPost.getReviewImages());
    }

    private Map<Long, String> setIdAndUrlAndCheckActive(List<ReviewImage> reviewImages) {
        return reviewImages.stream().
                filter(image->image.getIsActiveToDto()==1)
                .collect(Collectors.toMap(ReviewImage::getIdForUpdate, ReviewImage::getUrl));
    }

    private Map<Long, String> setIdAndUrl(List<ReviewImage> images) {
        if (images.size()==0){
            return null;
        }
        Map<Long,String> map = new LinkedHashMap<>();
        for (ReviewImage image:images) {
            map.put(image.getIdForUpdate(), image.getUrl());
        }
        return map;
    }
}
