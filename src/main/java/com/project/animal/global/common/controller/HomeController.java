package com.project.animal.global.common.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import static com.project.animal.global.common.constant.ViewName.INDEX_VIEW;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@Member MemberDto member, Model model) {
        if (member != null) model.addAttribute("member", member);

        return INDEX_VIEW;
    }
}
