package com.project.animal.review.domain;

import com.project.animal.member.domain.Member;
import com.project.animal.review.dto.CreateReviewPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Review")
@Getter
@NoArgsConstructor
public class ReviewPost {
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

    //사진 여러장 받을 수 있을 시에 List로 만들고 @OneToMany로 작성
    @OneToMany(mappedBy = "reviewPost")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Column(name = "is_active")
    private int isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

// TODO: comment 생성시 활성화
//    @OneToMany(mappedBy = "reviewPost")
//    private List<ReviewComment> comments;

    public ReviewPost(CreateReviewPostDto createReviewPostDto, Member member) {
        this.content = createReviewPostDto.getContent();
        this.title = createReviewPostDto.getTitle();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.member = member;
        this.viewCount = 0;
        this.isActive = 1;
    }

    public int increaseViewCount() {
        return this.viewCount++;
    }
}
