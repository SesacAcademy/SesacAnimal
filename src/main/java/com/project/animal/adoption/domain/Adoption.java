package com.project.animal.adoption.domain;

import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "adoption")
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

    @OneToMany(mappedBy = "adoption")
    private List<AdoptionImage> adoptionImages;

    @NotNull
    @NotBlank
    private String content; // 내용

    private int hit; // 조회수

    private String status; // "보호 중" // processStatus

    private String breed; // 견종/ 묘종 // kindCd와 매핑

    private String color; // colodCd와 매핑
    
    @NotNull
    @NotBlank
    private String gender; // 성별 //sexCd와 매핑

    private String age; // 나이 // age와 매핑

    @NotNull
    @NotBlank
    private String neutered; // 중성화 유무 // neuterYn

    @NotNull
    @NotBlank
    private String center; // 센터 유무

    private String isActive;

    private String centerAddr; // 센터 주소
    private String centerName; // 센터 이름

    private String happenPlace; // 발견장소
    private String desertionNo; // 공고 번호 (저장용)
    private String noticeSdt; // 공고시작일
    private String noticeEdt; // 공고종료일
    private String noticeNo; // 게시번호 ex) "경남-합천-2023-00487"
    private String specialMark; // 특징 ex) "마을배회"


    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;



    // 입양 게시판 write시 생성자
    public Adoption(String title, String breed, String gender, String age, String center, String neutered, String content, String color, String happenPlace, String specialMark) {
        this.title=title;
        this.breed=breed;
        this.gender=gender;
        this.age=age;
        this.center=center;
        this.neutered=neutered;
        this.content=content;
        this.color=color;
        this.happenPlace=happenPlace;
        this.specialMark=specialMark;
        this.status="보호중";
        this.createdAt = LocalDateTime.now();
    }


    // openApi 센터에서 입력되는 DB 생성자
    public Adoption(String desertionNo, String kindCd, String colorCd, String age, String noticeSdt, String noticeEdt, String processState, String sexCd, String neuterYn, String careAddr, String careNm, String noticeNo, String specialMark, String happenPlace) {
        this.desertionNo=desertionNo;
        this.breed=kindCd;
        this.color=colorCd;
        this.age=age;
        this.noticeSdt=noticeSdt;
        this.noticeEdt=noticeEdt;
        this.status=processState;
        this.gender=sexCd;
        this.neutered=neuterYn;
        this.centerAddr=careAddr;
        this.centerName=careNm;
        this.title=noticeNo;
        this.happenPlace=happenPlace;
        this.content=kindCd+"/"+colorCd+"/"+specialMark;
        this.center="Y";
        this.specialMark=specialMark;
        this.createdAt = LocalDateTime.now();
    }
}
