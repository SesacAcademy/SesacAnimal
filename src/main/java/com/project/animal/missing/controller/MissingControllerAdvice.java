package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.controller.utils.PathMaker;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.InvalidCreateFormException;
import com.project.animal.missing.exception.PostSaveFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackageClasses = MissingController.class)
public class MissingControllerAdvice {

  private final PathMaker pathMaker;

  public final int SUCCESS_FLAG = 1;

  public final int FAIL_FLAG = 0;


  @ExceptionHandler(PostSaveFailException.class)
  public String handlePostSaveFail(PostSaveFailException ex, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);
    redirectAttributes.addFlashAttribute("isSuccess", FAIL_FLAG);
    redirectAttributes.addFlashAttribute("errors", ex.getMessage());
    redirectAttributes.addFlashAttribute("msg", "입력한 정보를 다시 확인해주세요.");
    redirectAttributes.addFlashAttribute("post", ex.getInvalidForm());

    log.error("handlePostSaveFail: >> save fail: >> " + ex.getMessage());
    return "redirect:" + "/v1/missing/new";
  }

  @ExceptionHandler(InvalidCreateFormException.class)
  public String handleInvalidCreateForm(InvalidCreateFormException ex, RedirectAttributes redirectAttributes) {

    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);
    redirectAttributes.addFlashAttribute("isSuccess", FAIL_FLAG);
    redirectAttributes.addFlashAttribute("errors", errors);
    redirectAttributes.addFlashAttribute("msg", "입력한 정보를 다시 확인해주세요.");
    redirectAttributes.addFlashAttribute("post", ex.getInvalidForm());

    log.error("handleInvalidCreateForm: >> Invalid Input " + errors.toString());
    return "redirect:" + "/v1/missing/new";
  }

  @ExceptionHandler(DetailNotFoundException.class)
  public String handleDetailNotFound(DetailNotFoundException ex, Model model) {

    String listEndPoints = pathMaker.createLink("list").get("list");

    model.addAttribute("error", "Fail to find detail");
    model.addAttribute("type", "detail");
    model.addAttribute("redirectUrl", listEndPoints);

    log.error("DetailNotFoundException: >>  " + ex.getMissingId());
    return ViewName.POST_DETAIL;
  }
}
