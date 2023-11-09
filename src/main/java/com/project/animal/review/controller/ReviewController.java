package com.project.animal.review.controller;

import com.project.animal.global.common.dto.ImageListDto;
import com.project.animal.member.domain.Member;
import com.project.animal.review.constant.EndPoint;
import com.project.animal.review.constant.ViewName;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.CreateReviewPostDto;
import com.project.animal.review.dto.ReadListGeneric;

import com.project.animal.review.dto.ReadOneReviewDto;
import com.project.animal.review.dummy.CreateMemberWithoutSecurity;
import com.project.animal.review.service.ReviewImageService;
import com.project.animal.review.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(EndPoint.REVIEW)
@AllArgsConstructor
@Log4j2
public class ReviewController {


    private final ReviewService reviewService;

    private final ReviewImageService reviewImageService;
    private final int size = 10;

    @GetMapping(EndPoint.WRITE)
    public String writeReviewPage(){
        return ViewName.WRITE_PAGE;
    }
    @PostMapping(EndPoint.WRITE)
    public String createReview(@ModelAttribute CreateReviewPostDto createReviewPostDto,
                               BindingResult bindingResult,
                               @RequestParam(name = "imageList") List<MultipartFile> imageFiles
    ){
        ImageListDto imageListDto = new ImageListDto(imageFiles);
        CreateMemberWithoutSecurity ex = new CreateMemberWithoutSecurity();
        Member member = ex.createMember();
        if (bindingResult.hasErrors()){
            return ViewName.WRITE_PAGE;
        }
        ReviewPost reviewPost = reviewService.createReviewPost(createReviewPostDto, member);
        reviewImageService.saveImg(imageListDto, reviewPost);

      return "redirect:/review";
    }
    @GetMapping()
    public String readByCreatedAt(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model){

        ReadListGeneric viewDto = reviewService.readAll(page, size);
        model.addAttribute("listDto", viewDto);
        return ViewName.REVIEW_LIST;
    }
    // 단일로 하나 보기
     @GetMapping(EndPoint.ONE)
    public String readOne(@RequestParam(name = "reviewPostId") Long reviewPostId, Model model){
         ReadOneReviewDto readOneReviewDto = reviewService.readOne(reviewPostId);
         model.addAttribute("reviewDto", readOneReviewDto);
         return ViewName.READ_ONE;
    }
    // 검색
    @GetMapping(EndPoint.SEARCH)
    public String readBySearch(@RequestParam(name = "type") String type,
                               @RequestParam(name = "keyword") String keyword,
                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                               Model model) {
        ReadListGeneric<ReadListGeneric> viewDto = reviewService.readBySearch(type, keyword, page, size);
        model.addAttribute("listDto", viewDto);
        return ViewName.REVIEW_LIST;
    }
    @GetMapping(EndPoint.EDIT)
    public String edit(@RequestParam(name = "reviewPostId") Long reviewPostId, Model model){
        ReadOneReviewDto readOneReviewDto = reviewService.readOne(reviewPostId);
        model.addAttribute("reviewDto", readOneReviewDto);
        return ViewName.EDIT_ONE;
    }
    @Transactional
    @PostMapping(EndPoint.UPDATE)
    public String update(@RequestParam(name = "imageIds") List<Long> imageIds,
                         @ModelAttribute CreateReviewPostDto updatePostDto,
                         @RequestParam(name = "reviewPostId")Long reviewPostId){
        reviewService.update(imageIds, updatePostDto, reviewPostId);
        return "redirect:/review";
    }

}
