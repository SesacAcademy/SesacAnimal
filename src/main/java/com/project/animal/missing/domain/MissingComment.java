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

  // TODO: relation
  private long post_id;

  private String content;

  // TODO: relation
  private Long parentId;

  public MissingComment(long member_id, long post_id, String content) {
    this.member_id = member_id;
    this.post_id = post_id;
    this.content = content;
  }

  public MissingComment(long member_id, long post_id, String content, Long parentId) {
    this.member_id = member_id;
    this.post_id = post_id;
    this.content = content;
    this.parentId = parentId;
  }

  public MissingComment(long comment_id, long member_id, long post_id, String content) {
    this.comment_id = comment_id;
    this.member_id = member_id;
    this.post_id = post_id;
    this.content = content;
  }

  public MissingComment(long comment_id, long member_id, long post_id, String content, Long parentId) {
    this.comment_id = comment_id;
    this.member_id = member_id;
    this.post_id = post_id;
    this.content = content;
    this.parentId = parentId;
  }
}
