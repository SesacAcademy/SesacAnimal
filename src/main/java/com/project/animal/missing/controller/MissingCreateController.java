package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.MissingNewDto;
import com.project.animal.missing.exception.InvalidCreateFormException;
import com.project.animal.missing.exception.PostSaveFailException;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.NEW)
public class MissingCreateController {
  private final int SUCCESS_FLAG = 1;

  private final int FAIL_FLAG = 0;

  private final MissingPostService missingPostService;

  public MissingCreateController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }

  @GetMapping
  public String getPostNew(@Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br) {
    return ViewName.POST_NEW;
  }

  @PostMapping(EndPoint.CREATE)
  public String handleCreateRequest(@Valid @ModelAttribute("post") MissingNewDto dto, BindingResult br, RedirectAttributes redirectAttributes) {
    if (br.hasErrors()) {
      throw new InvalidCreateFormException(dto, br);
    }

    boolean isSuccess = missingPostService.createPost(dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING + EndPoint.NEW;
  }

  @ExceptionHandler(PostSaveFailException.class)
  public String handlePostSaveFail(PostSaveFailException ex, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);
    redirectAttributes.addFlashAttribute("isSuccess", FAIL_FLAG);
    redirectAttributes.addFlashAttribute("errors", ex.getMessage());
    redirectAttributes.addFlashAttribute("msg", "입력한 정보를 다시 확인해주세요.");
    redirectAttributes.addFlashAttribute("post", ex.getInvalidForm());

    log.error("handlePostSaveFail: >> Invalid Input " + ex.getMessage());
    return "redirect:" + EndPoint.MISSING + EndPoint.NEW;
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
    return "redirect:" + EndPoint.MISSING + EndPoint.NEW;
  }


}
