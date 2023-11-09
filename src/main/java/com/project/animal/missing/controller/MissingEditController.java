package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.dto.MissingEditDto;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.InvalidEditFormException;
import com.project.animal.missing.exception.PostEditFailException;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.EDIT)
public class MissingEditController {

  private final int SUCCESS_FLAG = 1;

  private final int FAIL_FLAG = 0;

  private final MissingPostService missingPostService;


  public MissingEditController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }

  @GetMapping(EndPoint.PATH_ID)
  public String showEditView(@PathVariable(EndPoint.ID_KEY) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    model.addAttribute("detail", detail);

    return ViewName.POST_EDIT;
  }

  @PutMapping(EndPoint.PATH_ID)
  public String handleEditRequest(@Valid @ModelAttribute("detail") MissingEditDto dto, BindingResult br, RedirectAttributes redirectAttributes) {
    if (!br.hasErrors()) {
      throw new InvalidEditFormException(dto, br);
    }

    boolean isSuccess = missingPostService.editPost(dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING + "/" + dto.getMissingId();
  }

  @ExceptionHandler(InvalidEditFormException.class)
  public String handleInvalidEditFormException(InvalidEditFormException ex, RedirectAttributes redirectAttributes) {
    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);
    redirectAttributes.addFlashAttribute("isSuccess", FAIL_FLAG);
    redirectAttributes.addFlashAttribute("errors", errors);
    redirectAttributes.addFlashAttribute("msg", "입력한 정보를 다시 확인해주세요.");
    redirectAttributes.addFlashAttribute("post", ex.getInvalidForm());

    log.error("InvalidEditFormException: >> Invalid Input " + errors.toString());
    return "redirect:" + EndPoint.MISSING + "/edit" + '/' + ex.getInvalidForm().getMissingId();
  }

  @ExceptionHandler(PostEditFailException.class)
  public String handlePostEditFail(PostEditFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to edit post");
    redirectAttributes.addFlashAttribute("type", "edit");

    return "redirect:" + EndPoint.MISSING + "/" + ex.getId();
  }

  @ExceptionHandler(DetailNotFoundException.class)
  public String handleDetailNotFound(DetailNotFoundException ex, Model model) {
    model.addAttribute("error", "Fail to find detail");
    model.addAttribute("type", "detail");

    return ViewName.POST_DETAIL;
  }

}
