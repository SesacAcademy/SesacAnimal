package com.project.animal.missing.exception;

import com.project.animal.missing.dto.MissingCommentNewDto;
import lombok.Getter;

@Getter
public class CommentSaveFailException extends RuntimeException {
  private MissingCommentNewDto invalidForm;
  public CommentSaveFailException(String message, Throwable cause, MissingCommentNewDto dto) {
    super(message, cause);
    this.invalidForm = dto;
  }
}
