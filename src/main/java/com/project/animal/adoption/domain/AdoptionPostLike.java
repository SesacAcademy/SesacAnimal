package com.project.animal.adoption.domain;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "adoption_post_like")
public class AdoptionPostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private Adoption adoption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;


    public void setMember(Member member) {
        this.member = member;
    }

    public void setMember(MemberDto memberDto) {
        Member member1 = new Member();
        member1.setEmail(memberDto.getEmail());
        member1.setId(memberDto.getId());
        member1.setNickname(memberDto.getNickname());
        member1.setRole(memberDto.getRole());
        this.member = member1;
    }
    public AdoptionPostLike(Adoption adoption, Member member){
        this.adoption = adoption;
        this.member = member;
    }
}
