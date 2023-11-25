package com.project.animal.adoption.domain;

import com.project.animal.adoption.dto.AdoptionEditDto;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.entity.BaseEntity;
import com.project.animal.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
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
@Setter
public class Adoption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //희정수정
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @NotBlank
    private String title; // 제목

    @OneToMany(mappedBy = "adoption", fetch = FetchType.LAZY)
    private List<AdoptionImage> adoptionImages;

    @OneToMany(mappedBy = "adoption", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AdoptionPostLike> adoptionPostLikes;

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

    @Column(name = "center_addr")
    private String centerAddr; // 센터 주소
    @Column(name = "center_name")
    private String centerName; // 센터 이름

    @Column(name = "happen_place")
    private String happenPlace; // 발견장소

    @Column(name = "desertion_no")
    private String desertionNo; // 공고 번호 (저장용)

    @Column(name = "notice_sdt")
    private String noticeSdt; // 공고시작일

    @Column(name = "notice_edt")
    private String noticeEdt; // 공고종료일

    @Column(name = "notice_no")
    private String noticeNo; // 게시번호 ex) "경남-합천-2023-00487"

    @Column(name = "special_mark")
    private String specialMark; // 특징 ex) "마을배회"

    @NotNull
    @Column(name = "is_active")
    private int isActive;

    public void setMember(Member member) {
        this.member = member;
    }


    public void setHit(int hit) {
        this.hit = hit;
    }

    public void changeIsActive(int isActive) {
        this.isActive = isActive;
    }
    
    // 입양 게시판 상세 수정 시 사용
    public void updateAdoption(AdoptionEditDto adoptionEditDto){
        this.title= adoptionEditDto.getTitle();
        this.content=adoptionEditDto.getContent();
        this.status=adoptionEditDto.getStatus();
        this.breed=adoptionEditDto.getBreed();
        this.color=adoptionEditDto.getColor();
        this.gender=adoptionEditDto.getGender();
        this.age=adoptionEditDto.getAge();
        this.neutered=adoptionEditDto.getNeutered();
        this.center=adoptionEditDto.getCenter();
        this.happenPlace=adoptionEditDto.getHappenPlace();
        this.specialMark=adoptionEditDto.getSpecialMark();
//        this.adoptionImages=adoptionEditDto.getPath();

    }


    // 입양 게시판 write시 생성자
    public Adoption(String title, String breed, String gender, String age, String center, String neutered, String content, String color, String happenPlace, String specialMark, Member member) {
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
        this.isActive=1;
        this.member = member;
        this.centerAddr=happenPlace;
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
        this.isActive=1;
        this.specialMark=specialMark;
    }
}
