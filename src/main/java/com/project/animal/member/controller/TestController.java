package com.project.animal.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        System.out.println("예아!");
        return "ok";
    }

    @RequestMapping("/tttt")
    @ResponseBody
    public String test2() {
        System.out.println("예아!222");
        return "ok";
    }

    @RequestMapping("/test3")
    @ResponseBody
    public String test3() {
        throw new RuntimeException();
    }
}
