package com.project.animal.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/v1/auth/login")
    public String test() {
        return "/member/login";
    }

    @GetMapping("/v1/auth/signup")
    public String test2() {
        return "/member/signup";
    }
}
