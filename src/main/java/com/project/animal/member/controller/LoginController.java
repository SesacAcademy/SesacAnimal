package com.project.animal.member.controller;

import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping(EndPoint.LOGIN)
    public String loginForm() {
        return ViewName.LOGIN_VIEW;
    }

    @PostMapping(EndPoint.LOGIN_API)
    @ResponseBody
    public String login() {
        return null;
    }
}
