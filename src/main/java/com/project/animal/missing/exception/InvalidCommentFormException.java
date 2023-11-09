package com.project.animal.missing.exception;

import com.project.animal.missing.dto.MissingCommentNewDto;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class InvalidCommentFormException extends RuntimeException {
  private final BindingResult bindingResult;
  private final MissingCommentNewDto invalidForm;

  public InvalidCommentFormException(MissingCommentNewDto invalidForm, BindingResult br) {
    this.invalidForm = invalidForm;
    this.bindingResult = br;
  }
}
