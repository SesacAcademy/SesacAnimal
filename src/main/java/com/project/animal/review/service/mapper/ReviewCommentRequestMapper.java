package com.project.animal.review.service.mapper;

import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewComment;
import com.project.animal.review.exception.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ReviewCommentRequestMapper {

    public ReviewComment dtoToEntity(CreateReviewComment dto, ReviewPost reviewPost) {
        ReviewComment reviewComment = new ReviewComment();
        Member member = memberNullCheck(reviewPost);
        reviewComment.dtoToEntity(dto, reviewPost, member);
        return reviewComment;
    }
    private Member memberNullCheck(ReviewPost reviewPost){
        if (reviewPost.getMember() != null){
            return reviewPost.getMember();
        }
        throw new NotFoundException("게시글을 작성한 멤버가 존재하지 않습니다. 게시글 아이디: "+ reviewPost.getId());
    }
}
