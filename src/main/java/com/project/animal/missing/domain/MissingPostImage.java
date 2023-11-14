package com.project.animal.missing.domain;

import com.project.animal.global.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
@Table(name ="Missing_image")
public class MissingPostImage extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long imageId;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private MissingPost missingPost;

  @NotNull
  @Column(name = "path")
  private String path;

  @Column(name ="is_active", columnDefinition = "1")
  private int isActive = 1;

  public MissingPostImage(String path, MissingPost missingPost) {

    this.path = path;
    this.missingPost = missingPost;

  }

  public void inactivatePostImage() {
    final int INACTIVE = 0;
    this.isActive = INACTIVE;
  }
}
