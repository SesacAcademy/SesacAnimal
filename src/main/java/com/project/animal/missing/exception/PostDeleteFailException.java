package com.project.animal.missing.exception;

import lombok.Getter;

@Getter
public class PostDeleteFailException extends RuntimeException {

  private long targetId;
  public PostDeleteFailException(String message, Throwable cause, long id) {
    super(message, cause);
    this.targetId = id;
  }
}
