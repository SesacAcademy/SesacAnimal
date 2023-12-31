package com.project.animal.missing.domain;

import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name ="Missing_post_like")
public class MissingLike extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long missingLikeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "missing_id")
  private MissingPost post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(name = "status")
  private int status;

  public MissingLike(MissingPost post, Member member, int status) {
    this.post = post;
    this.member = member;
    this.status = status;
  }

  public void toggleStatus() {
    this.status = this.status == 1 ? 0 : 1;
  }
}
