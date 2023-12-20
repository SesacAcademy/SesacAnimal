package com.project.animal.review.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.domain.ReviewPostLike;
import com.project.animal.review.exception.NotFoundException;
import com.project.animal.review.repository.ReviewPostLikeRepository;
import com.project.animal.review.service.mapper.ReviewPostLikeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class ReviewPostLikeService {
    private final ReviewPostLikeRepository reviewPostLikeRepository;
    private final MemberRepository memberRepository;
    private final ReviewService reviewService;
    private final ReviewPostLikeMapper reviewPostLikeMapper;

    /**
     * 좋아요 생성, 삭제를 담당하는 Service
     *
     * @version 0.1
     * @author 손승범
     * @Param MemberDto 좋아요 누른 Member DTO
     * @Param reviewPostId 수정하고자 하는 게시글의 아이디
     * */
    public void checkLikeStatus(MemberDto memberDto, Long reviewPostId) {
        Long memberId = memberDto.getId();
        Optional<ReviewPostLike> reviewPostLikeOptional = reviewPostLikeRepository.findByMemberIdAndReviewPostId(memberId, reviewPostId);
        likeStatusDecide(reviewPostLikeOptional,memberId, reviewPostId);
    }
    private void likeStatusDecide(Optional<ReviewPostLike> likeOptional, Long memberId, Long reviewPostId){
      likeOptional.ifPresent(like->delete(like));
      likeOptional.orElseGet(()->{
          insertLike(memberId, reviewPostId);
          return null;
      });
    }

    private void insertLike(Long memberId, Long reviewPostId) {
        Member member = findMemberById(memberId);
        ReviewPost reviewPost = reviewService.findById(reviewPostId);
        ReviewPostLike reviewPostLike = reviewPostLikeMapper.createEntity(member, reviewPost);
        reviewPostLikeRepository.save(reviewPostLike);
    }


    private void delete(ReviewPostLike reviewPostLike){
        reviewPostLikeRepository.delete(reviewPostLike);
    }
    private Member findMemberById(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(
                ()->new NotFoundException("해당 ID와 일치하는 회원이 없습니다:"+memberId));
    }
}
