package com.project.animal.global.common.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class OpenApiDto {

    private String desertionNo; // 유기번호

    private String filename; // 이미지 path

    private String happenDt; // 접수일 ex)20231107 yyyyMMdd

    private String happenPlace; // 발견 장소

    private String kindCd; // 묘종/견종  ex)[개] 푸들

    private String colorCd; // 색상

    private String age; // ex)2022(년생)

    private String weight; // ex) 4.5(Kg)

    private String noticeNo; // ex)충남-공주-2022-0001

    private String noticeSdt; //공고 시작일
    private String noticeEdt; //공고 종료일

    private String popfile; // 썸네일? ex) http://www.animal.go.kr/files/shelter/2021/12/202201041101258.png

    private String processState; //ex) 보호중

    private String sexCd; // ex) 성별 F

    private String neuterYn; // 중성화유무 Y, N, U 미상

    private String specialMark; // 특징 ex) 베이지색 조끼, 단미, 소심

    private String careNm; // 보호소 이름 ex)펫앤쉘터동물병원

    private String careTel; // 보호소 전화번호 ex) 031-714-8392

    private String careAddr; // 보호소 주소 ex) 경기도 성남시 분당구 불정로 266 (수내동, 유신제일조합)

    private String orgNm; // 관할기관 ex) 경기도 성남시

    private String chargeNm; // 관할기관 담당자 ex) 성남시청 동물보호팀

    private String officetel; // 관할기관 담당자 전화번호 ex) 031-729-3289

    @Column(name = "create_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updatedAt;


    public OpenApiDto(String desertionNo, String filename, String happenDt, String happenPlace, String kindCd, String colorCd, String age, String weight, String noticeNo, String noticeSdt, String noticeEdt, String popfile, String processState, String sexCd, String neuterYn, String specialMark, String careNm, String careTel, String careAddr, String orgNm, String chargeNm, String officetel) {
        this.desertionNo = desertionNo;
        this.filename = filename;
        this.happenDt = happenDt;
        this.happenPlace = happenPlace;
        this.kindCd = kindCd;
        this.colorCd = colorCd;
        this.age = age;
        this.weight = weight;
        this.noticeNo = noticeNo;
        this.noticeSdt = noticeSdt;
        this.noticeEdt = noticeEdt;
        this.popfile = popfile;
        this.processState = processState;
        this.sexCd = sexCd;
        this.neuterYn = neuterYn;
        this.specialMark = specialMark;
        this.careNm = careNm;
        this.careTel = careTel;
        this.careAddr = careAddr;
        this.orgNm = orgNm;
        this.chargeNm = chargeNm;
        this.officetel = officetel;
        this.createdAt = LocalDate.now();
    }
}
