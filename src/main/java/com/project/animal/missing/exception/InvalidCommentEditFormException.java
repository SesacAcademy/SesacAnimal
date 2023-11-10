package com.project.animal.missing.exception;

import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class InvalidCommentEditFormException extends RuntimeException {
  private final BindingResult bindingResult;
  private final MissingCommentEditDto invalidForm;

  public InvalidCommentEditFormException(MissingCommentEditDto invalidForm, BindingResult br) {
    this.invalidForm = invalidForm;
    this.bindingResult = br;
  }
}
