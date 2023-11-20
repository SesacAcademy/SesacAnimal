package com.project.animal.review.domain;

import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Review_Post_like")
@Getter
@NoArgsConstructor
public class ReviewPostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewPost reviewPost;

    public ReviewPostLike(Member member, ReviewPost reviewPost) {
        this.member = member;
        this.reviewPost = reviewPost;
//        isActive = 1;
    }

//    public void changeIsActive(int i) {
//        this.isActive = i;
//    }
}
