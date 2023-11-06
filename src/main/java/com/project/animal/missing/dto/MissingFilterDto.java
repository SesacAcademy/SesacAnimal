package com.project.animal.missing.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MissingFilterDto {
  private static final String DEFAULT_TIME = "T00:00:00";
  private String animalType;

  private String specifics;

  private String color;

  private LocalDateTime fromDate;

  private LocalDateTime endDate;

  private String search;

  public MissingFilterDto(String animalType, String specifics, String color, String fromDate, String endDate, String search) {
    this.animalType = animalType;
    this.specifics = specifics;
    this.color = color;
    this.fromDate = formatDate(fromDate);
    this.endDate = formatDate(endDate);
    this.search = search;
  }

  private LocalDateTime formatDate(String date) {
    if (date == null || date.isEmpty() || date.isBlank()) return null;
    return LocalDateTime.parse(date + DEFAULT_TIME);
  }
}
