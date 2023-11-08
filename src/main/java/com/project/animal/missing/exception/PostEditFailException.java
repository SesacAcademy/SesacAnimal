package com.project.animal.missing.exception;

import com.project.animal.missing.dto.MissingEditDto;
import com.project.animal.missing.dto.MissingNewDto;
import lombok.Getter;

@Getter
public class PostEditFailException extends RuntimeException {
  private MissingEditDto invalidForm;

  public PostEditFailException(String message, Throwable cause, MissingEditDto dto) {
    super(message, cause);
    this.invalidForm = dto;
  }
}
