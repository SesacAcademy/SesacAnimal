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

    private String url;

    //이미지 1장 이상 등록 가능한 경우, @ManyToOne 작성 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewPost reviewPost;
    public String getUrl(){
        return this.url;
    }
    public Long getIdForUpdate(){
        return this.id;
    }

}
