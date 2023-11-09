package com.project.animal.missing.exception;

import com.project.animal.missing.dto.MissingNewDto;
import lombok.Getter;

@Getter
public class PostSaveFailException extends RuntimeException {
  private MissingNewDto invalidForm;
  public PostSaveFailException(String message, Throwable cause, MissingNewDto dto) {
    super(message, cause);
    this.invalidForm = dto;
  }
}
