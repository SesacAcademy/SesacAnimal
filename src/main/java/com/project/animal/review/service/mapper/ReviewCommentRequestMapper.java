package com.project.animal.review.service.mapper;

import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewComment;
import com.project.animal.review.dto.ReviewCommentResponseDto;
import com.project.animal.review.exception.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ReviewCommentRequestMapper {

    public ReviewComment dtoToReviewComment(CreateReviewComment dto, ReviewPost reviewPost) {
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
    public ReviewCommentResponseDto reviewCommentToDto(ReviewComment reviewComment){
        Member member = memberNullCheckByComment(reviewComment);
        return new ReviewCommentResponseDto(reviewComment.getId(), reviewComment.getContent(),
                member.getNickname(), member.getId());
    }

    private Member memberNullCheckByComment(ReviewComment reviewComment) {
        if (reviewComment.getMember() != null){
            return reviewComment.getMember();
        }
        throw new NotFoundException("해당 댓글을 작성한 멤버가 존재하지 않습니다. 리뷰 댓글 아이디: "+ reviewComment.getId());
    }
}
