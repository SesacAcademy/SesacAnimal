package com.project.animal.review.service;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.MemberService;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.domain.ReviewPostLike;
import com.project.animal.review.repository.ReviewPostLikeRepository;
import com.project.animal.review.service.mapper.ReviewPostLikeMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewPostLikeServiceTest {

    @Mock
    private ReviewPostLikeRepository reviewPostLikeRepository;

    @Mock
    private ReviewService reviewService;

    @Mock
    private MemberService memberService;

    @Mock
    private ReviewPostLikeMapper reviewPostLikeMapper;

    @InjectMocks
    private ReviewPostLikeService reviewPostLikeService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이미 좋아요를 누른 게시글에 좋아요 삭제 테스트")
    void checkLikeStatus_WhenLikeExists_ShouldDeleteLike() {
        // Given
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setNickname("testNickname");
        member.setRole(Role.ROLE_USER);
        MemberDto mockMember = new MemberDto(member);
        Long reviewPostId = 1L;

        ReviewPostLike reviewPostLike = new ReviewPostLike();

        given(reviewPostLikeRepository.findByMemberIdAndReviewPostId(mockMember.getId(), reviewPostId))
                .willReturn(Optional.of(reviewPostLike));

        // When
        reviewPostLikeService.checkLikeStatus(mockMember, reviewPostId);

        // Then
        then(reviewPostLikeRepository).should().findByMemberIdAndReviewPostId(mockMember.getId(), reviewPostId);
        then(reviewPostLikeRepository).should().delete(reviewPostLike);
    }

    @Test
    @DisplayName("게시글 좋아요 생성 테스트")
    void checkLikeStatus_WhenLikeDoesNotExist_ShouldInsertLike() {
        // Given
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setNickname("testNickname");
        member.setRole(Role.ROLE_USER);
        MemberDto mockMember = new MemberDto(member);
        Long reviewPostId = 1L;
        ReviewPost reviewPost = new ReviewPost();

        given(reviewPostLikeRepository.findByMemberIdAndReviewPostId(mockMember.getId(), reviewPostId))
                .willReturn(Optional.empty());
        given(memberRepository.findById(mockMember.getId())).willReturn(Optional.of(member));
        given(reviewService.findById(reviewPostId)).willReturn(reviewPost);
        given(reviewPostLikeMapper.createEntity(member, reviewPost)).willReturn(new ReviewPostLike());

        // When
        reviewPostLikeService.checkLikeStatus(mockMember, reviewPostId);

        // Then
        then(reviewPostLikeRepository).should().findByMemberIdAndReviewPostId(mockMember.getId(), reviewPostId);
        then(reviewPostLikeRepository).should().save(any(ReviewPostLike.class));
    }
}
