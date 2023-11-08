package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.MissingCommentNewDto;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.InvalidCommentFormException;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;


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
//    String[] comments = {};

    Map<String, String> endPoints = createLinkConstants("edit", "delete", "newComment");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("post", detail);
    model.addAttribute("comments", null);

    return ViewName.POST_DETAIL;
  }

  @PostMapping(EndPoint.COMMENT + EndPoint.NEW)
  public String createNewComment(@Valid @ModelAttribute("comment") MissingCommentNewDto comment, BindingResult br) {
    if (br.hasErrors()) {
      throw new InvalidCommentFormException(comment, br);
    }

    log.info("test:>> " + comment);
    return "index";
  }

  @ExceptionHandler(DetailNotFoundException.class)
  public String handleDetailNotFound(DetailNotFoundException ex, Model model) {

    String listEndPoints = createLinkConstants("list").get("list");

    model.addAttribute("error", "Fail to find detail");
    model.addAttribute("type", "detail");
    model.addAttribute("redirectUrl", listEndPoints);

    log.error("DetailNotFoundException: >>  " + ex.getMissingId());
    return ViewName.POST_DETAIL;
  }

  @ExceptionHandler(InvalidCommentFormException.class)
  public String handleInvalidCommentFormException(InvalidCommentFormException ex, RedirectAttributes redirectAttributes) {
    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("error", "Fail to create comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("InvalidCommentFormException: >> Invalid comment Input " + errors.toString());
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + ex.getInvalidForm().getMissingId();
  }
}
