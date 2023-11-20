package com.project.animal.review.service.mapper;

import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.ReviewCommentDto;
import com.project.animal.review.dto.ReviewCommentDtoCount;
import com.project.animal.review.dto.ReviewCommentResponseDto;
import com.project.animal.review.exception.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class ReviewCommentRequestMapper {

    public ReviewComment dtoToReviewComment(ReviewCommentDto dto, ReviewPost reviewPost, Member member) {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.dtoToEntity(dto, reviewPost, member);
        return reviewComment;
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

    public ReviewCommentDtoCount includeCommentCount(List<ReviewCommentResponseDto> commentResponseList, List<ReviewComment> commentEntity) {
        int commentCount = commentCountCheck(commentEntity);
        return new ReviewCommentDtoCount(commentResponseList, commentCount);

    }
    private int commentCountCheck(List<ReviewComment> reviewComments) {
        if (reviewComments.size()!=0){
            return reviewComments.size();
        }return 0;
    }
}
