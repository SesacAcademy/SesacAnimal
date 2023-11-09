package com.project.animal.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewPostDto {
    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;
    @NotEmpty(message = "내용 입력은 필수입니다.")
    private String content;
}
