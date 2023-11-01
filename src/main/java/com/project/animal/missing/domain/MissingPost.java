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
  private char animalType;

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
  private char missingStatus;

  @NotNull
  @Column(name="is_active")
  private boolean isActive;

  @NotNull
  @Column(name="created_at")
  private LocalDateTime createdAt;

  @NotNull
  @Column(name="updated_at")
  private LocalDateTime updatedAt;
}
