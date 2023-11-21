package com.project.animal.missing.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.missing.dto.comment.MissingCommentDeleteDto;
import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import com.project.animal.missing.exception.InvalidCommentDeleteFormException;
import com.project.animal.missing.exception.InvalidCommentEditFormException;
import com.project.animal.missing.exception.InvalidCommentFormException;
import com.project.animal.missing.service.inf.MissingCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/missing/comment")
public class MissingCommentController {

  private final MissingCommentService missingCommentService;

  @PostMapping("/new")
  public String createNewComment(
          @Valid @ModelAttribute("comment") MissingCommentNewDto dto,
          @Member MemberDto member,
          BindingResult br) {

    if (br.hasErrors() || member == null) {
      throw new InvalidCommentFormException(dto, br);
    }

    missingCommentService.createComment(member.getId(), dto);
    return "redirect:" + "/v1/missing/detail/" + dto.getMissingId();
  }

  @PutMapping("/edit")
  public String editComment(
          @Valid @ModelAttribute("comment") MissingCommentEditDto dto,
          BindingResult br) {

    if (br.hasErrors()) {
      throw new InvalidCommentEditFormException(dto, br);
    }

    missingCommentService.editComment(dto);
    return "redirect:" + "/v1/missing/detail/" + dto.getMissingId();
  }


  @DeleteMapping("/delete")
  public String deleteComment(@Valid @ModelAttribute("comment") MissingCommentDeleteDto dto, BindingResult br) {
    if (br.hasErrors()) {
      throw new InvalidCommentDeleteFormException(dto, br);
    }

    missingCommentService.deleteComment(dto);

    return "redirect:" + "/v1/missing/detail/" + dto.getMissingId();
  }
}
