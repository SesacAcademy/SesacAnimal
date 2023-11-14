package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;
import com.project.animal.missing.exception.PostDeleteFailException;
import com.project.animal.missing.service.MissingPostServiceImpl;
import com.project.animal.missing.service.inf.MissingPostService;
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
  private final MissingPostService missingPostServiceImpl;


  public MissingDeleteController(MissingPostService missingPostServiceImpl) {
    this.missingPostServiceImpl = missingPostServiceImpl;
  }


  @DeleteMapping(EndPoint.PATH_ID)
  public String handleDeleteRequest(@PathVariable(EndPoint.ID_KEY) long id, RedirectAttributes redirectAttributes) {
    // TODO: id 없는 경우 검증
    boolean result = missingPostServiceImpl.deletePost(id);
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
