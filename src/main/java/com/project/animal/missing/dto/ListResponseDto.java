package com.project.animal.missing.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResponseDto<T> {
  private int totalCount;
  private List<T> list;

  public ListResponseDto(int totalCount, List<T> list) {
    this.totalCount = totalCount;
    this.list = list;
  }
}
