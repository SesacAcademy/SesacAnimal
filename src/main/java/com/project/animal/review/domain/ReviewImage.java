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
