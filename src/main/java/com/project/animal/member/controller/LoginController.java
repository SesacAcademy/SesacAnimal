package com.project.animal.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/v1/auth/login")
    public String loginForm() {
        return "/member/login";
    }

    @PostMapping("v1/auth/login")
    @ResponseBody
    public String login() {
        return null;
    }
}
