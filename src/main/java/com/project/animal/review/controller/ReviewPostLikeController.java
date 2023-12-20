package com.project.animal.review.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.review.constant.EndPoint;
import com.project.animal.review.service.ReviewPostLikeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/review")
@AllArgsConstructor
@Log4j2
public class ReviewPostLikeController {

    private final ReviewPostLikeService reviewPostLikeService;
    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }

    /**
     * 좋아요를 처리하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (좋아요를 누른게시글 뷰)
     */
    @PostMapping("/like/add")
    public String createLike(@Member MemberDto member,
                             @RequestParam(name = "reviewPostId") Long reviewPostId){
        log.info("past test");
        reviewPostLikeService.checkLikeStatus(member, reviewPostId);
        log.info("test ");
        return "redirect:review/one?reviewPostId=" + reviewPostId;
    }

}
