package com.project.animal.missing.exception;

import com.project.animal.missing.dto.MissingEditDto;
import com.project.animal.missing.dto.MissingNewDto;
import lombok.Getter;
import org.springframework.validation.BindingResult;


@Getter
public class InvalidEditFormException extends RuntimeException {
  private final BindingResult bindingResult;
  private final MissingEditDto invalidForm;

  public InvalidEditFormException(MissingEditDto invalidForm, BindingResult br) {
    this.invalidForm = invalidForm;
    this.bindingResult = br;
  }
}
