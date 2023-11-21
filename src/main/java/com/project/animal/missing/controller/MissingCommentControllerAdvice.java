package com.project.animal.missing.controller;


import com.project.animal.global.common.utils.BindingResultParser;
import com.project.animal.missing.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackageClasses = MissingCommentController.class)
public class MissingCommentControllerAdvice {

  // 댓글 생성 관련 예외
  @ExceptionHandler(CommentSaveFailException.class)
  public String handlePostSaveFail(CommentSaveFailException ex, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("error", "Fail to create comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("CommentSaveFailException: >> Fail to DB save " + ex.getMessage());
    return "redirect:" + "/v1/missing/detail/" + ex.getInvalidForm().getMissingId();
  }

  @ExceptionHandler(InvalidCommentFormException.class)
  public String handleInvalidCommentFormException(InvalidCommentFormException ex, RedirectAttributes redirectAttributes) {
    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("error", "Fail to create comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("InvalidCommentFormException: >> Invalid comment Input " + errors.toString());
    return "redirect:" + "/v1/missing/detail/" + ex.getInvalidForm().getMissingId();
  }


  // 댓글 상세 관련 예외
  @ExceptionHandler(CommentNotFoundException.class)
  public String handleCommentNotFoundException(CommentNotFoundException ex, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("error", "Fail to find comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("CommentNotFoundException: >> Fail to find comment id: >> " + ex.getCommentId());
    return "redirect:" + "/v1/missing/detail/" + ex.getMissingId();
  }


  // 댓글 수정 관련 예외
  @ExceptionHandler(CommentEditFailException.class)
  public String handleCommentEditFailException(CommentEditFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to edit comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("CommentNotFoundException: >> Fail to find comment id: >> " + ex.getInvalidForm().getCommentId());
    return "redirect:" + "/v1/missing/detail/" + ex.getInvalidForm().getMissingId();
  }

  @ExceptionHandler(InvalidCommentEditFormException.class)
  public String handleInvalidCommentEditFormException(InvalidCommentEditFormException ex, RedirectAttributes redirectAttributes) {
    Map<String, String> errors = BindingResultParser.parse(ex.getBindingResult());

    redirectAttributes.addFlashAttribute("error", "Fail to edit comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("InvalidCommentFormException: >> Invalid comment edit Input " + errors.toString());
    return "redirect:" + "/v1/missing/detail/" + ex.getInvalidForm().getMissingId();
  }


  // 댓글 삭제 관련 예외
  @ExceptionHandler(CommentDeleteFailException.class)
  public String handleCommentDeleteFailException(CommentDeleteFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to delete comment");
    redirectAttributes.addFlashAttribute("type", "comment");

    log.error("CommentDeleteFailException: >> Fail to delete comment id: >> " + ex.getInvalidForm().getCommentId());
    return "redirect:" + "/v1/missing/detail/" + ex.getInvalidForm().getMissingId();
  }

}
