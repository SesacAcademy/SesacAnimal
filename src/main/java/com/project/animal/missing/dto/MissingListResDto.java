package com.project.animal.missing.dto;

import com.project.animal.missing.domain.MissingPost;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MissingListResDto {

  private long id;

  private String title;

  // TODO: Change to member nickname;
  private long memberId;

  // TODO: Decide what to use either String or Enum;
  private String animalType;

  private String specifics;

  private String color;

  private int viewCount;

  private String missingPlace;

  private LocalDateTime missingTime;

  private int reward;

  private int missingStatus;

  private LocalDateTime updatedAt;

  private MissingListResDto(long id, String title, long memberId, String animalType, String specifics, String color, int viewCount, String missingPlace, LocalDateTime missingTime, int reward, int missingStatus, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.memberId = memberId;
    this.animalType = animalType;
    this.specifics = specifics;
    this.color = color;
    this.viewCount = viewCount;
    this.missingPlace = missingPlace;
    this.missingTime = missingTime;
    this.reward = reward;
    this.missingStatus = missingStatus;
    this.updatedAt = updatedAt;
  }

  public static MissingListResDto fromMissingPost(MissingPost post) {
    long id = post.getMissingId();
    String title = post.getTitle();
    long memberId = post.getMemberId();
    String animalType = post.getAnimalType();
    String specifics = post.getSpecifics();
    String color = post.getColor();
    int viewCount = post.getViewCount();
    String missingPlace = post.getMissingPlace();
    LocalDateTime missingTime = post.getMissingTime();
    int reward = post.getReward();
    int missingStatus = post.getMissingStatus();
    LocalDateTime updatedAt = post.getUpdatedAt();

    return new MissingListResDto(
            id, title, memberId, animalType, specifics, color, viewCount, missingPlace,
            missingTime, reward, missingStatus, updatedAt);
  }


}
