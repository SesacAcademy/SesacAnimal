package com.project.animal.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReviewCommentDtoCount {
    List<ReviewCommentResponseDto> reviewCommentResponseDtoList;
    int commentCount;
    public ReviewCommentDtoCount(List<ReviewCommentResponseDto> commentResponseList, int commentCount) {
        this.reviewCommentResponseDtoList = commentResponseList;
        this.commentCount = commentCount;
    }
}
