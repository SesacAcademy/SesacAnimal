package com.project.animal.adoption.domain;

import com.project.animal.adoption.dto.AdoptionCommentWriteDto;
import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "adoption_comment")
public class AdoptionComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="adoption_id")
    private Adoption adoption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(mappedBy = "parentId")
    private List<AdoptionComment> replies;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //    생성자
    public AdoptionComment(AdoptionCommentWriteDto adoptionCommentDto){
        this.content = adoptionCommentDto.getContent();
        this.parentId = adoptionCommentDto.getParentId();
        this.replies = adoptionCommentDto.getReplies();
    }



}
