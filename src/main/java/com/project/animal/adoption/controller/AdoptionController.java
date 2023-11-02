package com.project.animal.adoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adoption")
public class AdoptionController {


    @GetMapping
    public String adoptionMain(){

        return "adoption/adoption";
    }

}
