package com.project.animal.missing.dto;

import com.project.animal.global.common.utils.CustomDateParser;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class MissingFilterDto {
  private String animalType;

  private String specifics;

  private String color;
  private LocalDateTime fromDate;
  private LocalDateTime endDate;

  private String search;
  
  public void setFromDate(String fromDate) {
    this.fromDate = CustomDateParser.formatDate(fromDate);
  }

  public void setEndDate(String endDate) {
    this.endDate = CustomDateParser.formatDate(endDate);
  }
}
