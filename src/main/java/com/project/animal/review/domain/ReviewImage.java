package com.project.animal.review.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Review_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "review_id")
    //이미지 1장 이상 등록 가능한 경우, @ManyToOne 작성 필
    private Long reviewPostId;
    private String url;

}
