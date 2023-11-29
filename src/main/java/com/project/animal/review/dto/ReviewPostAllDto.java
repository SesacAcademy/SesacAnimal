package com.project.animal.review.dto;
import com.project.animal.review.domain.ReviewImage;
import com.project.animal.review.domain.ReviewPost;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private String nickname;

    private String url;

    private int commentCount;

    private int postLikeCount;

    public ReviewPostAllDto(ReviewPost reviewPost){
        this.reviewPostId = reviewPost.getId();
        this.title = reviewPost.getTitle();
        this.content = reviewPost.getContent();
        this.viewCount = reviewPost.getViewCount();
        this.updatedAt = reviewPost.getUpdatedAt();
        this.nickname = reviewPost.getMember().getNickname();
        this.url = getActiveImage(reviewPost.getReviewImages());
        this.commentCount = reviewPost.getComments().size();
        this.viewCount = reviewPost.getViewCount();
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

