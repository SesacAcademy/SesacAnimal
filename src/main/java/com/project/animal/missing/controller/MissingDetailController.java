package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.PostDeleteFailException;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.DETAIL)
public class MissingDetailController extends MissingController {

  private final MissingPostService missingPostService;


  public MissingDetailController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }

  @GetMapping(EndPoint.PATH_ID)
  public String getPostDetail(@PathVariable(EndPoint.ID_KEY) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    String[] comments = {"test1", "comments2"};

    Map<String, String> endPoints = createLinkConstants("edit", "delete");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("detail", detail);
    model.addAttribute("comments", comments);

    return ViewName.POST_DETAIL;
  }

  @ExceptionHandler(DetailNotFoundException.class)
  public String handleDetailNotFound(DetailNotFoundException ex, Model model) {
    model.addAttribute("error", "Fail to find detail");
    model.addAttribute("type", "detail");

    return ViewName.POST_DETAIL;
  }

}
