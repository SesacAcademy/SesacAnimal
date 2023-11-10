package com.project.animal.missing.controller;

import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.constant.ViewName;
import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import com.project.animal.missing.dto.MissingDetailDto;
import com.project.animal.missing.dto.comment.MissingCommentListEntryDto;
import com.project.animal.missing.exception.*;
import com.project.animal.missing.service.MissingCommentService;
import com.project.animal.missing.service.MissingPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.DETAIL)
public class MissingDetailController extends MissingController {

  private final MissingPostService missingPostService;

  private final MissingCommentService missingCommentService;

  @GetMapping(EndPoint.PATH_ID)
  public String getPostDetail(@PathVariable(EndPoint.ID_KEY) long postId, Model model) {
    MissingDetailDto detail = missingPostService.getPostDetail(postId);
    List<MissingCommentListEntryDto> comments = detail.getComments();

    Map<String, String> endPoints = createLinkConstants("edit", "delete", "newComment", "editComment");

    model.addAttribute("endPoints", endPoints);
    model.addAttribute("post", detail);
    model.addAttribute("comments", comments);

    return ViewName.POST_DETAIL;
  }

  @PostMapping(EndPoint.COMMENT + EndPoint.NEW)
  public String createNewComment(@Valid @ModelAttribute("comment") MissingCommentNewDto dto, BindingResult br) {
    if (br.hasErrors()) {
      throw new InvalidCommentFormException(dto, br);
    }

    missingCommentService.createComment(dto);
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + dto.getMissingId();
  }

  @PutMapping(EndPoint.COMMENT + EndPoint.EDIT)
  public String editComment(@Valid @ModelAttribute("comment") MissingCommentEditDto dto, BindingResult br) {
    if (br.hasErrors()) {
      throw new InvalidCommentEditFormException(dto, br);
    }

    missingCommentService.editComment(dto);
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + dto.getMissingId();
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

  @ExceptionHandler(CommentSaveFailException.class)
  public String handlePostSaveFail(CommentSaveFailException ex, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("error", "Fail to create comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("CommentSaveFailException: >> Fail to DB save " + ex.getMessage());
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + ex.getInvalidForm().getMissingId();
  }

  @ExceptionHandler(InvalidCommentEditFormException.class)
  public String handleInvalidCommentEditFormException(InvalidCommentEditFormException ex, RedirectAttributes redirectAttributes) {
    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("error", "Fail to edit comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("InvalidCommentFormException: >> Invalid comment edit Input " + errors.toString());
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + ex.getInvalidForm().getMissingId();
  }

  @ExceptionHandler(CommentNotFoundException.class)
  public String handleCommentNotFoundException(CommentNotFoundException ex, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("error", "Fail to find comment");
    redirectAttributes.addFlashAttribute("type", "detail");

    log.error("CommentNotFoundException: >> Fail to find comment id: >> " + ex.getCommentId());
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + ex.getMissingId();
  }

  @ExceptionHandler(CommentEditFailException.class)
  public String handleCommentEditFailException(CommentEditFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to edit comment");
    redirectAttributes.addFlashAttribute("type", "detail");

    log.error("CommentNotFoundException: >> Fail to find comment id: >> " + ex.getInvalidForm().getCommentId());
    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.DETAIL + "/" + ex.getInvalidForm().getMissingId();
  }
}
