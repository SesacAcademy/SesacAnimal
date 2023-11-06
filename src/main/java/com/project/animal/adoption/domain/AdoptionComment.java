package com.project.animal.adoption.domain;

import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AdoptionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



}
