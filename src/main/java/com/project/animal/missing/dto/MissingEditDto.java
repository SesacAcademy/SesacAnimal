package com.project.animal.missing.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.animal.global.common.utils.CustomDateParser;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
@ToString
public class MissingEditDto {

  @NotNull
  private long missingId;

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

  private List<Long> deletedIds;

  private MultipartFile[] images;

  public void setMissingTime (String missingTime) {
    this.missingTime = CustomDateParser.formatDate(missingTime);
  }

  public void setDeletedIds (String string) {
    ObjectMapper mapper = new ObjectMapper();

    List<Long> list = null;

    if (string.isEmpty()) {
      this.deletedIds = list;
      return;
    }

    try {
      list = mapper.readValue(string, new TypeReference<List<Long>>(){});
      Collections.sort(list);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.deletedIds = list;
  }
}
