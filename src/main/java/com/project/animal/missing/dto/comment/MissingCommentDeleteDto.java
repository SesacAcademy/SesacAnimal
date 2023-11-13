package com.project.animal.missing.dto.comment;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Data
@ToString
public class MissingCommentDeleteDto {
  @Min(1)
  long commentId;

  @Min(1)
  long missingId;

  Long parentId;
}
