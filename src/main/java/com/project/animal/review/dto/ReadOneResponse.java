package com.project.animal.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReadOneResponse {
    private ReadOneReviewDto readOneReviewDto;
    private List<ReviewCommentResponseDto> reviewCommentResponseDto;
}
