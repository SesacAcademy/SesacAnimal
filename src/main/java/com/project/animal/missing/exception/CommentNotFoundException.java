package com.project.animal.missing.exception;


import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {
  long commentId;
  long missingId;

  public CommentNotFoundException(long commentId, long missingId) {
    this.commentId = commentId;
    this.missingId = missingId;
  }


}
