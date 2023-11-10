package com.project.animal.adoption.dto;


import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdoptionEditDto {

    private String author;

    private String title;

    private List<AdoptionImage> path;

    private String breed;

    private String gender;

    private String age;

    private String center;

    private String color;

    private String happenPlace;

    private String neutered;

    private String specialMark;

    private String content;

    private String status;

    private Long postId;

    public AdoptionEditDto(Adoption adoption, Long postId){
        this.author = adoption.getMember().getName();
        this.title =adoption.getTitle();
        this.path=adoption.getAdoptionImages();
        this.breed=adoption.getBreed();
        this.gender=adoption.getGender();
        this.age=adoption.getAge();
        this.center=adoption.getCenter();
        this.color=adoption.getColor();
        this.happenPlace=adoption.getHappenPlace();
        this.neutered=adoption.getNeutered();
        this.specialMark=adoption.getSpecialMark();
        this.content=adoption.getContent();
        this.status=adoption.getStatus();
        this.postId=postId;
    }

    public AdoptionEditDto(Adoption adoption){
        this.author = adoption.getMember().getName();
        this.title =adoption.getTitle();
        this.path=adoption.getAdoptionImages();
        this.breed=adoption.getBreed();
        this.gender=adoption.getGender();
        this.age=adoption.getAge();
        this.center=adoption.getCenter();
        this.color=adoption.getColor();
        this.happenPlace=adoption.getHappenPlace();
        this.neutered=adoption.getNeutered();
        this.specialMark=adoption.getSpecialMark();
        this.content=adoption.getContent();
        this.status=adoption.getStatus();
    }

}
