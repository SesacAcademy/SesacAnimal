package com.project.animal.review.domain;

import com.project.animal.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


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

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Column(name = "view_count")
    private int viewCount;


    @Column(name = "is_active",  columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
// TODO: comment 생성시 활성화
//    @OneToMany(mappedBy = "reviewPost")
//    private List<ReviewComment> comments;

    public ReviewPost(String content, int viewCount) {
        this.content = content;
        this.viewCount = viewCount;
    }
}
