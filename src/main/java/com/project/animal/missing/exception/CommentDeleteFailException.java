package com.project.animal.missing.exception;

import com.project.animal.missing.dto.comment.MissingCommentDeleteDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import lombok.Getter;

@Getter
public class CommentDeleteFailException extends RuntimeException {
  private MissingCommentDeleteDto invalidForm;
  public CommentDeleteFailException(String message, Throwable cause, MissingCommentDeleteDto dto) {
    super(message, cause);
    this.invalidForm = dto;
  }
}
