package com.project.animal.adoption.domain;

import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @NotBlank
    private String title; // 제목

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private AdoptionImage adoptionImage;


    @NotNull
    @NotBlank
    private String content; // 내용
    
    private int hit; // 조회수

    private char status; // 0: 입양 가능 , 1: 입양 중, 2: 임시보호중, 3: 입양 대기중

    private String breed; // 견종/ 묘종

    @NotNull
    @NotBlank
    private String gender; // 성별

    private int age; // 나이

    @NotNull
    @NotBlank
    private String center;

    @NotNull
    @NotBlank
    private String neutered; // 중성화 유무

//    public Long getMemberId(Member member) {
//        return member.getId();
//    }



}
