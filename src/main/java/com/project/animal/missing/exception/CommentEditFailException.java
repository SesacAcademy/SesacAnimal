package com.project.animal.missing.exception;

import com.project.animal.missing.dto.comment.MissingCommentEditDto;
import com.project.animal.missing.dto.comment.MissingCommentNewDto;
import lombok.Getter;

@Getter
public class CommentEditFailException extends RuntimeException {
  private MissingCommentEditDto invalidForm;
  public CommentEditFailException(String message, Throwable cause, MissingCommentEditDto dto) {
    super(message, cause);
    this.invalidForm = dto;
  }
}
