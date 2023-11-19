package com.project.animal.global.common.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import static com.project.animal.global.common.constant.ViewName.INDEX_VIEW;

@Controller
public class HomeController {

    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }

    @GetMapping("/")
    public String home() {
        return INDEX_VIEW;
    }
}
