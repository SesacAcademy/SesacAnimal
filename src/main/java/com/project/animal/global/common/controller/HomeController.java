package com.project.animal.global.common.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingListEntryDto;
import com.project.animal.missing.service.inf.MissingPostService;
import com.project.animal.review.dto.ReviewIndexResponse;
import com.project.animal.review.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import static com.project.animal.global.common.constant.ViewName.INDEX_VIEW;

@Slf4j
@Controller
@AllArgsConstructor
public class HomeController {
    private final ReviewService reviewService;

    private final MissingPostService missingService;
    private final int MISSING_POST_CONTENTS_COUNT = 4;
    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }

    @GetMapping("/")
    public String home(Model model) {
        ListResponseDto<MissingListEntryDto> missingPosts = missingService.getPostsForHome(MISSING_POST_CONTENTS_COUNT);
        List<ReviewIndexResponse> listDto =  reviewService.readByLike();

        model.addAttribute("listDto",listDto);
        model.addAttribute("missingPosts", missingPosts.getList());

        return INDEX_VIEW;
    }

}
