package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.dto.MissingEditDto;
import com.project.animal.missing.exception.DetailNotFoundException;
import com.project.animal.missing.exception.InvalidEditFormException;
import com.project.animal.missing.exception.PostEditFailException;
import com.project.animal.missing.service.MissingPostServiceImpl;
import com.project.animal.missing.service.inf.MissingPostService;
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
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.EDIT)
public class MissingEditController extends MissingController {

  private final int SUCCESS_FLAG = 1;

  private final int FAIL_FLAG = 0;

  private final MissingPostService missingPostServiceImpl;


  public MissingEditController(MissingPostService missingPostServiceImpl) {
    this.missingPostServiceImpl = missingPostServiceImpl;
  }

  @GetMapping(EndPoint.PATH_ID)
  public String showEditView(@PathVariable(EndPoint.ID_KEY) long postId, Model model) {
    MissingDetailDto detail = missingPostServiceImpl.getPostDetail(postId);

    Map<String, String> endPoints = createLinkConstants("edit");
    model.addAttribute("detail", detail);
    model.addAttribute("endPoints", endPoints);

    return ViewName.POST_EDIT;
  }

  @PutMapping(EndPoint.PATH_ID)
  public String handleEditRequest(@Valid @ModelAttribute("detail") MissingEditDto dto, BindingResult br, RedirectAttributes redirectAttributes) {
    long memberId = 1;
    if (br.hasErrors()) {
      throw new InvalidEditFormException(dto, br);
    }

    boolean isSuccess = missingPostServiceImpl.editPost(memberId, dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + dto.getMissingId();
  }

  @ExceptionHandler(InvalidEditFormException.class)
  public String handleInvalidEditFormException(InvalidEditFormException ex, RedirectAttributes redirectAttributes) {
    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);
    redirectAttributes.addFlashAttribute("isSuccess", FAIL_FLAG);
    redirectAttributes.addFlashAttribute("errors", errors);
    redirectAttributes.addFlashAttribute("msg", "입력한 정보를 다시 확인해주세요.");
    redirectAttributes.addFlashAttribute("post", ex.getInvalidForm());

    log.error("InvalidEditFormException: >> Invalid Input: >> " + ex.getBindingResult().getFieldErrors());
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.EDIT + '/' + ex.getInvalidForm().getMissingId();
  }

  @ExceptionHandler(PostEditFailException.class)
  public String handlePostEditFail(PostEditFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to edit post");
    redirectAttributes.addFlashAttribute("type", "edit");

    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.EDIT + "/" + ex.getId();
  }

  @ExceptionHandler(DetailNotFoundException.class)
  public String handleDetailNotFound(DetailNotFoundException ex, Model model) {
    model.addAttribute("error", "Fail to find detail");
    model.addAttribute("type", "detail");

    return ViewName.POST_DETAIL;
  }

}
