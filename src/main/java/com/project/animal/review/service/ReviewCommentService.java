package com.project.animal.review.service;


import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewComment;

import com.project.animal.review.exception.NotFoundException;
import com.project.animal.review.exception.ReviewCommentException;

import com.project.animal.review.repository.ReviewCommentRepository;

import com.project.animal.review.service.mapper.ReviewCommentRequestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewCommentRequestMapper reviewCommentRequestMapper;


    public void createComment(CreateReviewComment commentDto, ReviewPost reviewPost) {
        dtoCheck(commentDto);
        ReviewComment commentEntity = reviewCommentRequestMapper.dtoToEntity(commentDto, reviewPost);
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

}

