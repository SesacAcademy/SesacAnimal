package com.project.animal.review.domain;

import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import com.project.animal.review.dto.CreateReviewPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Review")
@Getter
@NoArgsConstructor
public class ReviewPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Column(name = "view_count")
    private int viewCount;

    @OneToMany(mappedBy = "reviewPost")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Column(name = "is_active")
    private int isActive;

    @OneToMany(mappedBy = "reviewPost", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ReviewComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "reviewPost", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ReviewPostLike> reviewPostLikes = new ArrayList<>();

    public ReviewPost(CreateReviewPostDto createReviewPostDto, Member member) {
        this.content = createReviewPostDto.getContent();
        this.title = createReviewPostDto.getTitle();
        this.member = member;
        this.viewCount = 0;
        this.isActive = 1;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(CreateReviewPostDto dto) {
        this.content = dto.getContent();
        this.title = dto.getTitle();
    }
    public void changeStatus() {
        this.isActive = 0;
    }
}
