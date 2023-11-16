package com.project.animal.review.service.mapper;

import com.project.animal.review.dto.ReadOneResponse;
import com.project.animal.review.dto.ReadOneReviewDto;
import com.project.animal.review.dto.ReviewCommentResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class ReviewRequestMapper {
    public ReadOneResponse dtosToResponseDto(ReadOneReviewDto readOneReviewDto, List<ReviewCommentResponseDto> dtoList) {
        ReadOneResponse readOneResponse = new ReadOneResponse(readOneReviewDto, dtoList);
        return readOneResponse;
    }
}
