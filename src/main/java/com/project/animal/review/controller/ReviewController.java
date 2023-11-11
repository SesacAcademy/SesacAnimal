package com.project.animal.review.controller;

import com.project.animal.global.common.dto.ImageListDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.repository.MemberRepository;
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

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(EndPoint.REVIEW)
@AllArgsConstructor
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    private final ReviewImageService reviewImageService;
    private final int size = 10;

    private final MemberRepository memberRepository;

    private Member createMember(){
        CreateMemberWithoutSecurity ex = new CreateMemberWithoutSecurity();
        Optional<Member> member = memberRepository.findById(1L);
        return member.get();
    }

    @GetMapping(EndPoint.WRITE)
    public String writeReviewPage(){
        return ViewName.WRITE_PAGE;
    }
    @PostMapping(EndPoint.WRITE)
    public String createReviewPost(@ModelAttribute @Valid CreateReviewPostDto createReviewPostDto,
                               BindingResult bindingResult,
                               @RequestParam(name = "imageList") List<MultipartFile> imageFiles
    ){
        Member member = createMember();
        if (bindingResult.hasErrors()){
            return ViewName.WRITE_PAGE;
        }
        ReviewPost reviewPost = reviewService.createReviewPost(createReviewPostDto, member);
        reviewImageService.saveImg(imageFiles, reviewPost);

      return ViewName.HOME;
    }
    @GetMapping()
    public String readByCreatedAt(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                  Model model){
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
    @PostMapping(EndPoint.UPDATE)
    public String update(@RequestParam(name = "reviewImageIds", required = false) List<Long> reviewImageIds,
                         @ModelAttribute @Valid CreateReviewPostDto updatePostDto,
                         @RequestParam(name = "reviewPostId")Long reviewPostId,
                         @RequestParam(name = "imageList", required = false) List<MultipartFile> imageFiles,
                         BindingResult bindingResult, Model model
                         ){
        if (bindingResult.hasErrors()){
            log.info("update binding errors={}", bindingResult);
            model.addAttribute("updatePostDto", updatePostDto);
            model.addAttribute("org.springframework.validation.BindingResult.updatePostDto", bindingResult);
            return "redirect:/review/edit?reviewPostId=" + reviewPostId;
        }

        ReviewPost reviewPost = reviewService.updateReviewPost(updatePostDto, reviewPostId);
        reviewImageService.imageChangeStatus(reviewImageIds);
        reviewImageService.saveImg(imageFiles,reviewPost);
        return ViewName.HOME;
    }
    @GetMapping(EndPoint.DELETE)
    public String delete(@RequestParam(name = "reviewPostId")Long reviewPostId){
        reviewService.delete(reviewPostId);
        return ViewName.HOME;
    }

//    @GetMapping()
    //인자 검사
    public String createReviewComment(BindingResult bindingResult){
        //member 객체, reviewpost 객체 필
        return "redirect";
        // 해당페이지 리턴
    }
}
