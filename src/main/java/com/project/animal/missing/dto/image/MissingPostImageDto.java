package com.project.animal.missing.dto.image;

import lombok.Data;

@Data
public class MissingPostImageDto {
  private final String PREFIX = "http://infra.shucloud.site:8007/missing";
  private long imageId;

  private String path;

  public MissingPostImageDto(long imageId, String path) {
    this.imageId = imageId;
    this.path = PREFIX + path;
  }
}
