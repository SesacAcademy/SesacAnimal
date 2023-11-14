package com.project.animal.missing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


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
  @JoinColumn(name = "missing_id")
  private MissingPost missingPost;
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private MissingComment parentComment;

  @OneToMany(mappedBy = "parentComment")
  private List<MissingComment> comments;

  public MissingComment(long member_id, MissingPost missingPost, String content, MissingComment parentComment) {
    this.member_id = member_id;
    this.missingPost = missingPost;
    this.content = content;
    this.parentComment = parentComment;
  }

  public void changeComment(String comment) {

    this.content = comment;
  }
}
