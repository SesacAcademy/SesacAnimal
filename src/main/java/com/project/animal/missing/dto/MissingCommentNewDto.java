package com.project.animal.missing.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@ToString
public class MissingCommentNewDto {

  @Min(1)
  private long missingId;

  @Min(1)
  private long memberId;

  @NotBlank
  private String comment;
}
