package com.project.animal.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewComment {
    @NotEmpty(message = "입력 값이 없는 댓글은 생성할 수 없습니다.")
    // 글자 수 제한 정책이 있다면 논의 필@Column(length = 100)
    private String content;
    private Long parentId;
}