package com.project.animal.missing.dto.comment;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@ToString
public class MissingCommentEditDto {

  @Min(1)
  private long commentId;

  @Min(1)
  private long missingId;

  @Min(1)
  private long memberId;

  @NotBlank
  private String comment;

  private Long parentId;
}
