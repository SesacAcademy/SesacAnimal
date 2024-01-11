package com.project.animal.review.service;

import com.project.animal.review.domain.ReviewComment;
import com.project.animal.review.dto.ReviewCommentDtoCount;
import com.project.animal.review.dto.ReviewCommentResponseDto;
import com.project.animal.review.repository.ReviewCommentCustomRepository;
import com.project.animal.review.service.mapper.ReviewCommentRequestMapper;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCommentServiceTest {

    @InjectMocks
    private ReviewCommentService reviewCommentService;

    @Mock
    private ReviewCommentCustomRepository reviewCommentCustomRepository;

    @Mock
    private ReviewCommentRequestMapper reviewCommentRequestMapper;

    @Test
    @DisplayName("게시글 댓글, 대댓글 호출 서비스 테스트")
    public void testReadByReviewPostId() {
        // given
        Long reviewPostId = 1L;

        // 목(Mock) 객체 생성
        ReviewComment mockComment1 = mock(ReviewComment.class);
        ReviewComment mockComment2 = mock(ReviewComment.class);

        // 목(Mock) 객체를 담을 리스트 생성
        List<ReviewComment> mockReviewComments = new ArrayList<>();
        mockReviewComments.add(mockComment1);
        mockReviewComments.add(mockComment2);

        // 가정: 댓글에 대한 DTO가 있다고 가정
        ReviewCommentResponseDto mockResponseDto1 = mock(ReviewCommentResponseDto.class);
        ReviewCommentResponseDto mockResponseDto2 = mock(ReviewCommentResponseDto.class);

        // 목(Mock) DTO 객체를 담을 리스트 생성
        List<ReviewCommentResponseDto> mockCommentResponseList = new ArrayList<>();
        mockCommentResponseList.add(mockResponseDto1);
        mockCommentResponseList.add(mockResponseDto2);

        // 리포지토리 메소드를 목(Mock) 처리
        when(reviewCommentCustomRepository.findAllByPostId(reviewPostId)).thenReturn(mockReviewComments);

        // 매퍼 메소드를 목(Mock) 처리
        when(reviewCommentRequestMapper.reviewCommentToDto(any())).thenReturn(mockResponseDto1, mockResponseDto2);

        // when
        ReviewCommentDtoCount result = reviewCommentService.readByReviewPostId(reviewPostId);

        // then
        // 리포지토리 메소드가 기대한 파라미터로 호출되었는지 확인
        verify(reviewCommentCustomRepository).findAllByPostId(reviewPostId);

        // 매퍼 메소드가 각 댓글에 대해 기대한 횟수로 호출되었는지 확인
        verify(reviewCommentRequestMapper, times(mockReviewComments.size())).reviewCommentToDto(any());
    }



}
