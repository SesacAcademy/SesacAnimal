package com.project.animal.review.service;


import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewComment;

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


    public void createComment(CreateReviewComment commentDto, ReviewPost reviewPost) {
        dtoCheck(commentDto);
        ReviewComment commentEntity = reviewCommentRequestMapper.dtoToReviewComment(commentDto, reviewPost);
        log.info("id null 체크"+commentDto.getParentId());
        if (commentDto.getParentId() != null){
            ReviewComment parentComment =findReviewComment(commentDto.getParentId());
            commentEntity.updateParent(parentComment);
        }
        reviewCommentRepository.save(commentEntity);
    }
    private void dtoCheck(CreateReviewComment dto){
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

    public List<ReviewCommentResponseDto> readByReviewPostId(Long reviewPostId) {
        List<ReviewComment> reviewComments = reviewCommentCustomRepository.findAllByPost(reviewPostId);
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
        }); return commentResponseList;
    }
}


