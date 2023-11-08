package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.exception.PostDeleteFailException;
import com.project.animal.missing.service.MissingPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequestMapping(EndPoint.MISSING_BASE + EndPoint.DELETE)
public class MissingDeleteController {
  private final MissingPostService missingPostService;


  public MissingDeleteController(MissingPostService missingPostService) {
    this.missingPostService = missingPostService;
  }


  @DeleteMapping(EndPoint.PATH_ID)
  public String handleDeleteRequest(@PathVariable(EndPoint.ID_KEY) long id, RedirectAttributes redirectAttributes) {

    boolean result = missingPostService.deletePost(id);
    redirectAttributes.addFlashAttribute("serverMsg", "Success to delete the post");

    return "redirect:" + EndPoint.MISSING_BASE + EndPoint.LIST;
  }

  @ExceptionHandler(PostDeleteFailException.class)
  public String handlePostDeleteFail(PostDeleteFailException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", "Fail to delete post");
    redirectAttributes.addFlashAttribute("type", "delete");

    return "redirect:" + EndPoint.MISSING + "/" + ex.getTargetId();
  }

}
