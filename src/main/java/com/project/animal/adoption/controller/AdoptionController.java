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


    @GetMapping("/adoption-write")
    public String adoptionWrite(){

        return "adoption/adoption_write";
    }

    @GetMapping("/adoption-edit")
    public String adoptionEdit(){

        return "adoption/adoption_edit";
    }

    @GetMapping("/adoption-read")
    public String adoptionRead(){

        return "adoption/adoption_read";
    }

}
