package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.ListResponseDto;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.dto.MissingFilterDto;
import com.project.animal.missing.dto.MissingListResDto;
import com.project.animal.missing.dummy.MissingPostDummy;
import com.project.animal.missing.exceptions.DetailNotFound;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING)
public class MissingController {

  private final MissingPostService missingPostService;

  @Autowired
  public MissingController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }


  @GetMapping(EndPoint.LIST)
  public String getPostList(
          @ModelAttribute MissingFilterDto filterDto,
          @PageableDefault(sort="missing_id", direction = Sort.Direction.DESC)
          Pageable pageable,
          Model model) {

    ListResponseDto<MissingListResDto> result = missingPostService.getPostList(filterDto, pageable);

    model.addAttribute("list", result.getList());
    model.addAttribute("count", result.getTotalCount());

    return ViewName.POST_LIST;
  }



  @GetMapping(EndPoint.DETAIL)
  public String getPostDetail(@PathVariable(EndPoint.POST_ID) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    String[] comments = {"test1", "comments2"};

    model.addAttribute("detail", detail);
    model.addAttribute("comments", comments);

    return ViewName.POST_DETAIL;
  }

  @GetMapping(EndPoint.NEW)
  public String getPostNew() {
    return ViewName.POST_NEW;
  }


  @ExceptionHandler(DetailNotFound.class)
  public String handleDetailNotFound(DetailNotFound ex, Model model) {
    model.addAttribute("error", "Fail to find detail");

    return ViewName.POST_DETAIL;
  }

}
