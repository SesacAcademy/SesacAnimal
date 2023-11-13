package com.project.animal.adoption.controller;


import com.project.animal.adoption.dto.AdoptionCommentDto;
import com.project.animal.global.common.constant.EndPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdoptionCommentController {
    
    @GetMapping(EndPoint.ADOPTION_COMMENT)
    public String adoptionCommentGet () {

        return EndPoint.ADOPTION_READ;

    }
    // 댓글 쓰기
    @PostMapping(EndPoint.ADOPTION_COMMENT)
    public String adoptionCommentPost(@Validated @ModelAttribute AdoptionCommentDto adoptionCommentDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("adoption_read 영역 내 댓글 에러 ={}", bindingResult);

            return "redirect:"+EndPoint.ADOPTION_READ;
        }



        return "redirect:"+EndPoint.ADOPTION_READ;

    }
}
