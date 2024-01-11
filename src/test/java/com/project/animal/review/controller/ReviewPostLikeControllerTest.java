package com.project.animal.review.controller;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.review.service.ReviewPostLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@WebMvcTest(value = ReviewPostLikeController.class, useDefaultFilters = false)
public class ReviewPostLikeControllerTest {


    @Mock
    private ReviewPostLikeService reviewPostLikeService;

    @InjectMocks
    private ReviewPostLikeController reviewPostLikeController;


    @DisplayName("게시글 좋아요 테스트")
    @Test
    public void testCreateLikeGPT() {
        // Given
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setNickname("testNickname");
        member.setRole(Role.ROLE_USER);
        MemberDto mockMember = new MemberDto(member);
        Long reviewPostId = 1L;

        // When
        String result = reviewPostLikeController.createLike(mockMember, reviewPostId);

        // Then
        verify(reviewPostLikeService).checkLikeStatus(eq(mockMember), eq(reviewPostId));
        assertEquals("redirect:review/one?reviewPostId=" + reviewPostId, result);
    }
}
