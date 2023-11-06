package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewPost;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReviewPostAllDto {

    private String title;

    private String content;

    private int viewCount;

    private LocalDateTime createdAt;

    private String memberName;
    public ReviewPostAllDto(ReviewPost reviewPost){
        this.title = reviewPost.getTitle();
        this.content = reviewPost.getContent();
        this.viewCount = reviewPost.getViewCount();
        this.createdAt = reviewPost.getCreatedAt();
        this.memberName = reviewPost.getMember().getName();
    }
}