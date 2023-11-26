package com.project.animal.missing.dto;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.image.MissingPostImageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class MissingListEntryDto {

  private long id;

  private String title;

  private long memberId;

  private String author;

  private String animalType;

  private String specifics;

  private String color;

  private int viewCount;

  private String missingPlace;

  private LocalDateTime missingTime;

  private int reward;

  private int missingStatus;

  private LocalDateTime updatedAt;

  private List<MissingPostImageDto> images;

  private int likeCount;

  public MissingListEntryDto() {}

  private MissingListEntryDto(long id, String title, long memberId, String author, String animalType, String specifics, String color, int viewCount, String missingPlace, LocalDateTime missingTime, int reward, int missingStatus, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.memberId = memberId;
    this.author = author;
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

  public static MissingListEntryDto fromMissingPost(MissingPost post) {
    long id = post.getMissingId();
    String title = post.getTitle();
    long memberId = post.getMember().getId();
    String author = post.getMember().getNickname();
    String animalType = post.getAnimalType();
    String specifics = post.getSpecifics();
    String color = post.getColor();
    int viewCount = post.getViewCount();
    String missingPlace = post.getMissingPlace();
    LocalDateTime missingTime = post.getMissingTime();
    int reward = post.getReward();
    int missingStatus = post.getMissingStatus();
    LocalDateTime updatedAt = post.getUpdatedAt();


    return new MissingListEntryDto(
            id, title, memberId, author, animalType, specifics, color, viewCount, missingPlace,
            missingTime, reward, missingStatus, updatedAt);
  }

  public void addImages(List<MissingPostImageDto> images) {
    this.images = images;
  }

  public void addLikeCount(Integer count) {
    this.likeCount = count == null ? 0 : count;
  }
}
