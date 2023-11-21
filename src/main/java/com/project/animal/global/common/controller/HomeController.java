package com.project.animal.global.common.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.review.dto.ReviewIndexResponse;
import com.project.animal.review.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import static com.project.animal.global.common.constant.ViewName.INDEX_VIEW;

@Controller
@AllArgsConstructor
public class HomeController {
    private final ReviewService reviewService;


    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }

    @GetMapping("/")
    public String home() {
        return INDEX_VIEW;
    }


    public String homeReview(Model model) {
        List<ReviewIndexResponse> listDto =  reviewService.readByLike();
        model.addAttribute("listDto",listDto);
        return INDEX_VIEW;
    }

}
