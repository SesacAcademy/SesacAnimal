package com.project.animal.review.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.ReviewCommentDto;
import com.project.animal.review.service.ReviewCommentService;
import com.project.animal.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(MockitoExtension.class)
public class ReviewCommentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ReviewCommentController reviewCommentController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewCommentService reviewCommentService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(reviewCommentController).build();
    }
    @Test
    @DisplayName("댓글 생성 테스트")
    public void createReviewComment_댓글생성후리다이렉트() {
        // Given
        Long reviewPostId = 1L;
        ReviewCommentDto reviewCommentDto = new ReviewCommentDto();
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setNickname("testNickname");
        member.setRole(Role.ROLE_USER);
        MemberDto mockMember = new MemberDto(member);

        ReviewPost reviewPost = new ReviewPost();
        when(reviewService.findById(reviewPostId)).thenReturn(reviewPost);

        // When
        String result = reviewCommentController.createReviewComment(reviewCommentDto, bindingResult, reviewPostId, mockMember);


        // Then
        verify(bindingResult).hasErrors();
        verify(reviewCommentService).createComment(any(ReviewCommentDto.class), any(ReviewPost.class), any(MemberDto.class));
        assertEquals("redirect:/review/one?reviewPostId=" + reviewPostId, result);
    }

    @Test
    @DisplayName("댓글 생성_바인딩 에러 발생_댓글 생성x_리다이렉트")
    public void createReviewComment_바인딩에러시_댓글생성하지않고리다이렉트() {
        // Given
        Long reviewPostId = 1L;
        ReviewCommentDto reviewCommentDto = new ReviewCommentDto();
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setNickname("testNickname");
        member.setRole(Role.ROLE_USER);
        MemberDto mockMember = new MemberDto(member);

        when(bindingResult.hasErrors()).thenReturn(true);

        // When
        String result = reviewCommentController.createReviewComment(reviewCommentDto, bindingResult, reviewPostId, mockMember);

        // Then
        verify(bindingResult).hasErrors(); // bindingResult.hasErrors()가 호출되었는지 확인
        verify(reviewCommentService, never()).createComment(any(ReviewCommentDto.class), any(ReviewPost.class), any(MemberDto.class));
        assertEquals("redirect:/review/one?reviewPostId=" + reviewPostId, result);
    }
}
