package com.project.animal.missing.service.converter;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.MissingEditDto;
import com.project.animal.missing.dto.MissingNewDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DtoEntityConverter {
  public MissingPost toMissingPost(MissingNewDto dto) {
    long memberId = 1;
    String title = dto.getTitle();
    String animalType = dto.getAnimalType();
    String specifics = dto.getSpecifics();
    String color = dto.getColor();
    int viewCount = 0;
    String missingPlace = dto.getMissingPlace();
    LocalDateTime missingTime = dto.getMissingTime();
    String description = dto.getDescription();
    int reward = dto.getReward();
    int missingStatus = 0;
    int isActive = 1;

    return new MissingPost(memberId, title, animalType, specifics, color, viewCount,
    missingPlace, missingTime, description, reward, missingStatus, isActive);
  }

  public MissingPost toMissingPost(MissingEditDto dto) {
    long missingId = dto.getMissingId();
    long memberId = 1;
    String title = dto.getTitle();
    String animalType = dto.getAnimalType();
    String specifics = dto.getSpecifics();
    String color = dto.getColor();
    int viewCount = 0;
    String missingPlace = dto.getMissingPlace();
    LocalDateTime missingTime = dto.getMissingTime();
    String description = dto.getDescription();
    int reward = dto.getReward();
    int missingStatus = 0;
    int isActive = 1;

    return new MissingPost(missingId, memberId, title, animalType, specifics, color, viewCount,
            missingPlace, missingTime, description, reward, missingStatus, isActive);
  }
}
