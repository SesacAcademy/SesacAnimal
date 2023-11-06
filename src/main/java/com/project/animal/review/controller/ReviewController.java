package com.project.animal.review.controller;

import com.project.animal.member.domain.Member;
import com.project.animal.review.constant.EndPoint;
import com.project.animal.review.constant.ViewName;
import com.project.animal.review.dto.CreateReviewPostDto;
import com.project.animal.review.dto.ReadAllGeneric;
import com.project.animal.review.dummy.CreateMemberWithoutSecurity;
import com.project.animal.review.service.ReviewService;
import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

@Controller
@RequestMapping(EndPoint.REVIEW)
@AllArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;
    private final int size = 6;

    @GetMapping(EndPoint.WRITE)
    public String writeReviewPage(){
        return ViewName.WRITE_PAGE;
    }
    @PostMapping(EndPoint.WRITE)
    public String createReview(@ModelAttribute CreateReviewPostDto createReviewPostDto,
                               BindingResult bindingResult,
                               @RequestParam(name = "imageFile") MultipartFile file
    ){
        // 시큐리티 구현 형태에 따라 수정 예정
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Long memberId =  userDetails.get()
        CreateMemberWithoutSecurity ex = new CreateMemberWithoutSecurity();
        Member member = ex.createMember();
        // 시큐리티 구성시 위 2줄 코드 삭제 예정
        if (bindingResult.hasErrors()){
            return ViewName.WRITE_PAGE;
        }
        reviewService.createReviewPost(createReviewPostDto, file, member);
      return ViewName.REVIEW_LIST;
    }
    //1. 작성자 이름으로 가져오기 2. 전체 가져오기(desc 날짜) 3. 제목 4.내용  5.view 순 6.like순
    @GetMapping(EndPoint.LIST)
    public String readByCreatedAt(@PathVariable(name = "page") int page ,Model model){
        ReadAllGeneric viewDto = reviewService.readAll(page, size);
        model.addAttribute("ListDto", viewDto);
        return ViewName.REVIEW_LIST;
    }
}
