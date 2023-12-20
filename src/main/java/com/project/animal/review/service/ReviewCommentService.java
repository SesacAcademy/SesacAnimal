package com.project.animal.review.service;


import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.ReviewCommentDto;

import com.project.animal.review.dto.ReviewCommentDtoCount;
import com.project.animal.review.dto.ReviewCommentResponseDto;
import com.project.animal.review.exception.NotFoundException;
import com.project.animal.review.exception.ReviewCommentException;

import com.project.animal.review.repository.ReviewCommentCustomRepository;
import com.project.animal.review.repository.ReviewCommentRepository;

import com.project.animal.review.service.mapper.ReviewCommentRequestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewCommentRequestMapper reviewCommentRequestMapper;
    private final ReviewCommentCustomRepository reviewCommentCustomRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 작성을 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param commentDto 댓글 DTO
     * @Param reviewPost 게시글
     * @Param MemberDto 멤버 DTO
     * */
    public void createComment(ReviewCommentDto commentDto, ReviewPost reviewPost, MemberDto memberDto) {
        dtoCheck(commentDto);
        Member member = findMember(memberDto);
        ReviewComment commentEntity = reviewCommentRequestMapper.dtoToReviewComment(commentDto, reviewPost, member);
        if (commentDto.getParentId() != null){
            ReviewComment parentComment =findReviewComment(commentDto.getParentId());
            commentEntity.updateParent(parentComment);

        }
        reviewCommentRepository.save(commentEntity);
    }
    private void dtoCheck(ReviewCommentDto dto){
        if (dto.getContent()==null){
            throw new ReviewCommentException("입력값이 없는 댓글은 생성할 수 없습니다.");
        }
    }
    private ReviewComment commentCheckOptional(Optional<ReviewComment> comment ,Long reviewCommentId){
        return comment.orElseThrow(
                ()->new ReviewCommentException("해당 댓글의 아이디와 일치하는 댓글이 없습니다. 유효하지 않은 reviewCommentId: "+ reviewCommentId));
    }
    private ReviewComment findReviewComment(Long reviewCommentId){
        Optional<ReviewComment> reviewCommentOptional = reviewCommentRepository.findById(reviewCommentId);
        return commentCheckOptional(reviewCommentOptional, reviewCommentId);
    }
    private Member findMember(MemberDto memberDto){
        Long memberId = memberDto.getId();
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(()->new NotFoundException("해당 id의 멤버를 조회할 수 없습니다."+memberId));
    }

    /**
     * 댓글 계층구조를  담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param reviewPostId 게시글 id
     * */
    public ReviewCommentDtoCount readByReviewPostId(Long reviewPostId) {
        List<ReviewComment> reviewComments = reviewCommentCustomRepository.findAllByPostId(reviewPostId);;
        List<ReviewCommentResponseDto> commentResponseList = new ArrayList<>();
        Map<Long, ReviewCommentResponseDto> commentHashMap = new HashMap<>();
        reviewComments.forEach(c->{
            ReviewCommentResponseDto reviewCommentResponseDto = reviewCommentRequestMapper.reviewCommentToDto(c);
            commentHashMap.put(reviewCommentResponseDto.getReviewCommentId(), reviewCommentResponseDto);
            if (c.getParentComment()!=null){
                commentHashMap.get(c.getParentComment().getId()).getChildren().add(reviewCommentResponseDto);
            }else {
                commentResponseList.add(reviewCommentResponseDto);
            }
        });
        return reviewCommentRequestMapper.includeCommentCount(commentResponseList, reviewComments);
    }


    /**
     * 댓글 수정을 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param reviewPostId 게시글 id
     * @Param ReviewCommentDto 수정하고자 하는 DTO
     * */
    public void update(Long reviewCommentId, ReviewCommentDto dto) {
        dtoCheck(dto);
        ReviewComment reviewComment = findReviewComment(reviewCommentId);
        reviewComment.update(dto);
    }
    /**
     * 댓글 삭제를 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param reviewPostId 게시글 id
     * */
    public void delete(Long reviewCommentId) {
        ReviewComment reviewComment = findReviewComment(reviewCommentId);
        reviewCommentRepository.delete(reviewComment);
    }
}


