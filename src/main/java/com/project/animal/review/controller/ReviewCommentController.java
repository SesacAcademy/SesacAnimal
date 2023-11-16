package com.project.animal.review.controller;

import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.review.constant.EndPoint;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.ReviewCommentDto;
import com.project.animal.review.dummy.CreateMemberWithoutSecurity;
import com.project.animal.review.service.ReviewCommentService;
import com.project.animal.review.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(EndPoint.REVIEW)
@AllArgsConstructor
@Log4j2
public class ReviewCommentController {

    private final MemberRepository memberRepository;
    private final ReviewService reviewService;
    private final ReviewCommentService reviewCommentService;


    private Member createMember(){
        CreateMemberWithoutSecurity ex = new CreateMemberWithoutSecurity();
        Optional<Member> member = memberRepository.findById(1L);
        return member.get();
    }
    @PostMapping(EndPoint.ADD_COMMENT)
    //1. 글자 수 검사 => 바인딩 리절트 2.게시판 읽어올 때 comment에 관한 내용도 던져야 함 id, parentId, content, CreatedAt,
    public String createReviewComment(@ModelAttribute @Valid ReviewCommentDto reviewCommentDto,
                                      BindingResult bindingResult,
                                      @RequestParam(name = "reviewPostId") Long reviewPostId,
                                      Model model) {
        {
            if (bindingResult.hasErrors()) {
                log.info("binding error: "+bindingResult.toString());
                return "redirect:" + "/review" + EndPoint.REVIEW_READ_ONE + "?reviewPostId=" + reviewPostId;
            }
            ReviewPost reviewPost = reviewService.findPostAndMember(reviewPostId);
            reviewCommentService.createComment(reviewCommentDto, reviewPost);
            return "redirect:" + "/review" + EndPoint.REVIEW_READ_ONE + "?reviewPostId=" + reviewPostId;
        }
    }
    @PostMapping(EndPoint.COMMENT_UPDATE)
    public String commentUpdate(@RequestParam("reviewCommentId")Long reviewCommentId,
                                @RequestParam("reviewPostId")Long reviewPostId,
                                @ModelAttribute ReviewCommentDto dto,
                                BindingResult bindingResult,
                                Model model){
        if (bindingResult.hasErrors()) {
            log.info("binding error: "+bindingResult.toString());
            return "redirect:" + "/review" + EndPoint.REVIEW_READ_ONE + "?reviewPostId=" + reviewPostId;
        }
        reviewCommentService.update(reviewCommentId, dto);
        return "redirect:" + "/review" + EndPoint.REVIEW_READ_ONE + "?reviewPostId=" + reviewPostId;
    }
    //1. 댓글 전체 리드 멤버 객체가 필요하다 그걸로 닉네임 가져올 수 있음 -> 게시글 리드에서 적합해보임
    //2. 댓글 업데이트
    //3. 댓글 삭제

}
