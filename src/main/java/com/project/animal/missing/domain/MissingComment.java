package com.project.animal.missing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
@Table(name ="Missing_comment")

public class MissingComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long comment_id;

  // TODO: relation
  private long member_id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private MissingPost missingPost;
  private String content;

  // TODO: relation
  private Long parentId;
  public MissingComment(long member_id, MissingPost missingPost, String content, Long parentId) {
    this.member_id = member_id;
    this.missingPost = missingPost;
    this.content = content;
    this.parentId = parentId;
  }

  public void changeComment(String comment) {
    this.content = comment;
  }
}
