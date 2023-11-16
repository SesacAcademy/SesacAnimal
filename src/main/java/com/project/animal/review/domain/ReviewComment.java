package com.project.animal.review.domain;

import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import com.project.animal.review.dto.ReviewCommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Review_comment")
@Getter
@NoArgsConstructor
public class ReviewComment extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "review_comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewPost reviewPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ReviewComment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(
            mappedBy = "parentComment",
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<ReviewComment> subReviewComments = new ArrayList<>();
    public void dtoToEntity(ReviewCommentDto dto, ReviewPost reviewPost, Member member) {
        this.content = dto.getContent();
        this.reviewPost = reviewPost;
        this.member = member;
    }

    public void updateParent(ReviewComment parentComment) {
        this.parentComment = parentComment;
    }

    public void update(ReviewCommentDto dto) {
        this.content = dto.getContent();
    }
}
