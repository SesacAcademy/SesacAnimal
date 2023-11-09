package com.project.animal.missing.dto;

import com.project.animal.global.common.utils.CustomDateParser;
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
    this.fromDate = CustomDateParser.formatDate(fromDate);
    this.endDate = CustomDateParser.formatDate(endDate);
    this.search = search;
  }

}
