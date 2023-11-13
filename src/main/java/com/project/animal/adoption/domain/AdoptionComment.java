package com.project.animal.adoption.domain;

import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "adoption_comment")
public class AdoptionComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="adoption_id")
    private Adoption adoption;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private String content;

    private int group_num;
    private int tab;


//    @Column(name = "create_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "update_at")
//    private LocalDateTime updatedAt;


}
