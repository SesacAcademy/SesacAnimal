package com.project.animal.adoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("v1/adoption")
public class AdoptionController {


    @GetMapping
    public String adoptionMain(){

        return "adoption/adoptionList";
    }


    @GetMapping("/edit")
    public String adoptionWrite(){

        return "adoption/adoption_write";
    }

    @GetMapping("/edit/{id}")
    public String adoptionEdit(){

        return "adoption/adoption_edit";
    }

    @GetMapping("/{id}")
    public String adoptionRead(){

        return "adoption/adoption_read";
    }

}
