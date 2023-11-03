package com.project.animal.review.controller;

import com.project.animal.review.constant.EndPoint;
import com.project.animal.review.constant.ViewName;
import com.project.animal.review.dto.CreateReviewPostDto;
import com.project.animal.review.service.ReviewService;
import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
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

    @GetMapping(EndPoint.WRITE)
    public String writeReviewPage(){
        return ViewName.WRITE_PAGE;
    }
    @PostMapping(EndPoint.WRITE)
    public String createReview(@ModelAttribute CreateReviewPostDto createReviewPostDto,
                               BindingResult bindingResult,
                               @RequestParam(name = "imageFile") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (bindingResult.hasErrors()){
            return ViewName.WRITE_PAGE;
        }
        reviewService.createReviewPost(createReviewPostDto, file);
      return ViewName.REVIEW_LIST;
    }
}
