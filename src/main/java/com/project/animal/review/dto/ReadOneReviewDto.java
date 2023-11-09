package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadOneReviewDto {
    private String title;
    private String name;
    private LocalDateTime updatedAt;
    private String content;
    private int viewCount;
    private Map<Long,String> urls = new LinkedHashMap<>();
    private Long reviewPostId;

    public ReadOneReviewDto(ReviewPost reviewPost, int viewCount) {
        this.title = reviewPost.getTitle();
        this.reviewPostId = reviewPost.getId();
        this.name = reviewPost.getMember().getName();
        this.updatedAt = reviewPost.getUpdatedAt();
        this.content = reviewPost.getContent();
        this.viewCount = viewCount;
        this.urls = setIdAndUrl(reviewPost.getReviewImages());
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
