package com.project.animal.missing.dto;

import com.project.animal.global.common.utils.CustomDateParser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class MissingNewDto {

  @NotBlank
  private String title;

  @NotBlank
  private String animalType;

  @NotBlank
  private String specifics;

  @NotBlank
  private String color;

  @NotBlank
  private String missingPlace;

  @NotNull
  private LocalDateTime missingTime;

  private int reward;

  @NotBlank
  private String description;



  public void setMissingTime (String missingTime) {
    this.missingTime = CustomDateParser.formatDate(missingTime);
  }
}
