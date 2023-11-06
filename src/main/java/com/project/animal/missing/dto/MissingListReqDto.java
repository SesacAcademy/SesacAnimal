package com.project.animal.missing.dto;

import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@ToString
public class MissingListReqDto {
  private static final int defaultLimit = 10;
  private static final int defaultPage = 1;
  private static final String defaultTime = "T00:00:00";

  @NotNull(message = "limit를 입력해주세요")
  private Integer limit;

  @NotNull(message = "page를 입력해주세요")
  private Integer page;

  private String animalType;

  private String specifics;

  private String color;

  private LocalDateTime fromDate;

  private LocalDateTime endDate;

  private String search;

  public MissingListReqDto(Integer limit, Integer page, String animalType, String specifics, String color, String fromDate, String endDate, String search) {
    this.limit = limit == null ? defaultLimit : limit;
    this.page = page == null ? defaultPage : page;
    this.animalType = animalType;
    this.specifics = specifics;
    this.color = color;
    this.fromDate = formatDate(fromDate);
    this.endDate = formatDate(endDate);
    this.search = search;
  }

  private LocalDateTime formatDate(String date) {
    if (date == null || date.isEmpty() || date.isBlank()) return null;
    return LocalDateTime.parse(date + defaultTime);
  }
}
