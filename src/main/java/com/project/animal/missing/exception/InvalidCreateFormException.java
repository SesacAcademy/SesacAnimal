package com.project.animal.missing.exception;

import com.project.animal.missing.dto.MissingNewDto;
import lombok.Getter;
import org.springframework.validation.BindingResult;


@Getter
public class InvalidCreateFormException extends RuntimeException {
  private final BindingResult bindingResult;
  private final MissingNewDto invalidForm;

  public InvalidCreateFormException(MissingNewDto invalidForm, BindingResult br) {
    this.invalidForm = invalidForm;
    this.bindingResult = br;
  }
}
