package com.project.animal.missing.exception;

import com.project.animal.missing.dto.comment.MissingCommentDeleteDto;
import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class InvalidCommentDeleteFormException extends RuntimeException {
  private final BindingResult bindingResult;
  private final MissingCommentDeleteDto invalidForm;

  public InvalidCommentDeleteFormException(MissingCommentDeleteDto invalidForm, BindingResult br) {
    this.invalidForm = invalidForm;
    this.bindingResult = br;
  }
}
