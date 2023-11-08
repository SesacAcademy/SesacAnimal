package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.*;
import com.project.animal.missing.exception.*;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING)
public class MissingController {

  private final int SUCCESS_FLAG = 1;

  private final int FAIL_FLAG = 0;

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
  public String getPostDetail(@PathVariable(EndPoint.ID) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    String[] comments = {"test1", "comments2"};

    model.addAttribute("detail", detail);
    model.addAttribute("comments", comments);

    return ViewName.POST_DETAIL;
  }

  @GetMapping(EndPoint.NEW)
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

  @GetMapping(EndPoint.EDIT)
  public String showEditView(@PathVariable(EndPoint.ID) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    model.addAttribute("detail", detail);

    return ViewName.POST_EDIT;
  }

  @PutMapping(EndPoint.EDIT)
  public String handleEditRequest(@Valid @ModelAttribute("detail") MissingEditDto dto, BindingResult br, RedirectAttributes redirectAttributes) {
    if (br.hasErrors()) {
      throw new InvalidEditFormException(dto, br);
    }

    boolean isSuccess = missingPostService.editPost(dto);
    redirectAttributes.addFlashAttribute("isSuccess", isSuccess ? SUCCESS_FLAG : FAIL_FLAG);
    redirectAttributes.addFlashAttribute("isRedirected", SUCCESS_FLAG);

    return "redirect:" + EndPoint.MISSING + "/" + dto.getMissingId();
  }

  @DeleteMapping(EndPoint.DELETE)
  public String handleDeleteRequest(@PathVariable(EndPoint.ID) long id, RedirectAttributes redirectAttributes) {
    boolean result = missingPostService.deletePost(id);

    redirectAttributes.addFlashAttribute("serverMsg", "Success to delete the post");

    return "redirect:" + EndPoint.MISSING + EndPoint.LIST;
  }


  @ExceptionHandler(DetailNotFoundException.class)
  public String handleDetailNotFound(DetailNotFoundException ex, Model model) {
    model.addAttribute("error", "Fail to find detail");
    model.addAttribute("type", "detail");

    return ViewName.POST_DETAIL;
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

  @ExceptionHandler(PostDeleteFailException.class)
  public String handlePostDeleteFail(PostDeleteFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to delete post");
    redirectAttributes.addFlashAttribute("type", "delete");

    return "redirect:" + EndPoint.MISSING + "/" + ex.getTargetId();
  }

}
