package com.project.animal.review.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
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
@RequestMapping("/review")
@AllArgsConstructor
@Log4j2
public class ReviewCommentController {

    private final ReviewService reviewService;
    private final ReviewCommentService reviewCommentService;


    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }
    @PostMapping("/comment/add")
    public String createReviewComment(@ModelAttribute @Valid ReviewCommentDto reviewCommentDto,
                                      BindingResult bindingResult,
                                      @RequestParam(name = "reviewPostId") Long reviewPostId,
                                      Model model) {
        {
            if (bindingResult.hasErrors()) {
                log.info("binding error: "+bindingResult.toString());
                return "redirect:/review/one?reviewPostId=" + reviewPostId;
            }
            ReviewPost reviewPost = reviewService.findPostAndMember(reviewPostId);
            reviewCommentService.createComment(reviewCommentDto, reviewPost);
            return "redirect:/review/one?reviewPostId=" + reviewPostId;
        }
    }
    @PostMapping("/comment/update")
    public String commentUpdate(@RequestParam("reviewCommentId")Long reviewCommentId,
                                @RequestParam("reviewPostId")Long reviewPostId,
                                @ModelAttribute ReviewCommentDto dto,
                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.info("binding error: "+bindingResult.toString());
            return "redirect:/review/one?reviewPostId=" + reviewPostId;
        }
        reviewCommentService.update(reviewCommentId, dto);
        return "redirect:/review/one?reviewPostId=" + reviewPostId;
    }
    @PostMapping("/comment/delete")
    public String commentDelete(@RequestParam("reviewCommentId")Long reviewCommentId,
                                @RequestParam("reviewPostId")Long reviewPostId){
        reviewCommentService.delete(reviewCommentId);
        return "redirect:/review/one?reviewPostId=" + reviewPostId;
    }

}
