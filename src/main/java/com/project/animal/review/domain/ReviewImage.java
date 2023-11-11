package com.project.animal.review.domain;

import com.project.animal.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Review_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImage extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewPost reviewPost;
    @Column(name = "is_active")
    private int isActive;
    public String getUrl(){
        return this.url;
    }
    public Long getIdForUpdate(){
        return this.id;
    }
    public int getIsActiveToDto(){
        return this.isActive;
    }

    public void changeStatus() {
        this.isActive = 0;
    }
}
