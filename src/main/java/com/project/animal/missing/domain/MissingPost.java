package com.project.animal.missing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name ="Missing")
public class MissingPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long missingId;

  // TODO: make a relation with member table
  @NotNull
  @Column(name="member_id")
  private long memberId;

  @NotNull
  @Column(name="title")
  private String title;

  @NotNull
  @Column(name="animal_type")
  private String animalType;

  @NotNull
  @Column(name="specifics")
  private String specifics;

  @NotNull
  @Column(name="color")
  private String color;

  @NotNull
  @Column(name="view_count")
  private int viewCount;

  @NotNull
  @Column(name="missing_place")
  private String missingPlace;

  @NotNull
  @Column(name="missing_time")
  private LocalDateTime missingTime;

  @NotNull
  @Column(name="description")
  private String description;

  @Column(name="reward")
  private int reward;

  @NotNull
  @Column(name="missing_status")
  private int missingStatus;

  @NotNull
  @Column(name="is_active")
  private boolean isActive;

  @Column(name="created_at")
  private LocalDateTime createdAt;

  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  // TODO: Comment 생성시 ManyToOne 으로 관계설정 하기
  //  private List<Comment> comments;

  public MissingPost(long missingId, long memberId, String title, String animalType, String specifics, String color, int viewCount, String missingPlace, LocalDateTime missingTime, String description, int reward, char missingStatus, boolean isActive) {
    this.missingId = missingId;
    this.memberId = memberId;
    this.title = title;
    this.animalType = animalType;
    this.specifics = specifics;
    this.color = color;
    this.viewCount = viewCount;
    this.missingPlace = missingPlace;
    this.missingTime = missingTime;
    this.description = description;
    this.reward = reward;
    this.missingStatus = missingStatus;
    this.isActive = isActive;
  }
}
