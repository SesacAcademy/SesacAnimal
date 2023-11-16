package com.project.animal.review.dto;

import com.project.animal.review.domain.ReviewComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReviewCommentResponseDto {
    private Long reviewCommentId;
    private String content;
    private String nickname;
    private Long memberId;
    private List<ReviewCommentResponseDto> children = new ArrayList<>();

    public ReviewCommentResponseDto(Long reviewCommentId, String content, String nickname, Long memberId) {
        this.reviewCommentId = reviewCommentId;
        this.content = content;
        this.nickname = nickname;
        this.memberId = memberId;
    }

}
