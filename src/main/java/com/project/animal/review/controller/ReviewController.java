package com.project.animal.review.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;

import com.project.animal.review.constant.ViewName;
import com.project.animal.review.domain.ReviewPost;
import com.project.animal.review.dto.*;

import com.project.animal.review.service.ReviewCommentService;
import com.project.animal.review.service.ReviewImageService;
import com.project.animal.review.service.ReviewService;
import com.project.animal.review.service.mapper.ReviewRequestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/review")
@AllArgsConstructor
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    private final ReviewImageService reviewImageService;
    private final int size = 9;

    private final ReviewCommentService reviewCommentService;

    private final ReviewRequestMapper reviewRequestMapper;

    @ModelAttribute("member")
    public MemberDto addMemberInModel(@Member MemberDto member) {
        return member;
    }

    /**
     * 게시글 작성 폼으로 이동하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 작성 뷰)
     */
    @GetMapping("/write")
    public String writeReviewPage(){
        return ViewName.WRITE_PAGE;
    }

    /**
     * 게시글 작성을 처리하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 리스트 뷰)
     */
    @PostMapping("/write")
    public String createReviewPost(@ModelAttribute @Valid CreateReviewPostDto createReviewPostDto,
                                    BindingResult bindingResult,
                                   @RequestParam(name = "imageList") List<MultipartFile> imageFiles,
                                   @Member MemberDto member){
        if (bindingResult.hasErrors()){
            return ViewName.WRITE_PAGE;
        }

        ReviewPost reviewPost = reviewService.createReviewPost(createReviewPostDto, member);
        reviewImageService.saveImg(imageFiles, reviewPost);
        return ViewName.HOME;
    }

    /**
     * 최신글 리스트를 보여주는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 리스트 뷰)
     */
    @GetMapping()
    public String readByCreatedAt(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model){
        ReadList viewDto = reviewService.readAll(page, size);
        model.addAttribute("listDto", viewDto);
        return ViewName.REVIEW_LIST;
    }

    /**
     * 단일 게시글로 이동하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (단일 게시글 뷰)
     */
    @GetMapping("/one")
    public String readOne(@RequestParam(name = "reviewPostId") Long reviewPostId, Model model){
         ReadOneReviewDto readOneReviewDto = reviewService.readOne(reviewPostId);
         ReviewCommentDtoCount dto = reviewCommentService.readByReviewPostId(reviewPostId);
         ReadOneResponse readOneResponse = reviewRequestMapper.dtosToResponseDto(readOneReviewDto,dto.getReviewCommentResponseDtoList());
         model.addAttribute("reviewDto", readOneResponse);
         model.addAttribute("commentCount", dto.getCommentCount());
         return ViewName.READ_ONE;
    }

    /**
     * 게시글 검색을 처리하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 리스트 뷰)
     */
    @GetMapping("/search")
    public String readBySearch(@RequestParam(name = "type") String type,
                               @RequestParam(name = "keyword") String keyword,
                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                               Model model) {

        ReadList viewDto = reviewService.readByKeyword(type, page, size, keyword);
        model.addAttribute("listDto", viewDto);
        model.addAttribute("type", type);         // 검색 유형 추가
        model.addAttribute("keyword", keyword);
        return ViewName.REVIEW_LIST_BY_SEARCH;
    }

    /**
     * 게시글 정렬(좋아요순, 조회순)을 처리하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 리스트 뷰)
     */
    @GetMapping("/filter")
    public String readByFilter(@RequestParam(name = "type") String type,
                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                               Model model) {
        ReadList viewDto = reviewService.readByFilter(type, page, size);
        model.addAttribute("listDto", viewDto);
        model.addAttribute("type", type);
        return ViewName.REVIEW_LIST_BY_FILTER;
    }

    /**
     * 게시글 수정폼으로 이동하게 하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 수정 뷰)
     */
    @GetMapping("/edit")
    public String edit(@RequestParam(name = "reviewPostId") Long reviewPostId, Model model){
        ReadOneReviewDto readOneReviewDto = reviewService.readOne(reviewPostId);
        ReviewCommentDtoCount dto = reviewCommentService.readByReviewPostId(reviewPostId);
        ReadOneResponse readOneResponse = reviewRequestMapper.dtosToResponseDto(readOneReviewDto,dto.getReviewCommentResponseDtoList());
        model.addAttribute("commentCount",dto.getCommentCount());
        model.addAttribute("reviewDto", readOneResponse);
        return ViewName.EDIT_ONE;
    }

    /**
     * 게시글 수정을 처리하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 리스트 뷰)
     */
    @PostMapping("/update")
    public String update(@RequestParam(name = "reviewImageIds", required = false) List<Long> reviewImageIds,
                         @ModelAttribute @Valid CreateReviewPostDto updatePostDto,
                         @RequestParam(name = "reviewPostId")Long reviewPostId,
                         @RequestParam(name = "imageList", required = false) List<MultipartFile> imageFiles,
                         BindingResult bindingResult, Model model){
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

    /**
     * 게시글 삭제를 처리하는 Controller
     *
     * @version 0.1
     * @author 손승범
     * @return String (게시글 리스트 뷰)
     */
    @GetMapping("/delete")
    public String delete(@RequestParam(name = "reviewPostId")Long reviewPostId){
        reviewService.delete(reviewPostId);
        return ViewName.HOME;
    }
}
