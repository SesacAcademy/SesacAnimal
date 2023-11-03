package com.project.animal.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewPostDto {
    private String title;
    private String content;
    private String nickname;
}
