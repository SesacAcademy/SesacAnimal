package com.project.animal.missing.exception;


import lombok.Getter;

@Getter
public class DetailNotFoundException extends RuntimeException{
  long missingId;

  public DetailNotFoundException(long missingId) {
    this.missingId = missingId;
  }



}
