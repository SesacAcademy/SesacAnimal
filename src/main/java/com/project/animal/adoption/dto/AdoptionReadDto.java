package com.project.animal.adoption.dto;


import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
public class AdoptionReadDto {

    private String author;
    private String title;
    private String breed;
    private String gender;
    private String age;
    private String center;
    private String color;
    private String happenPlace;
    private String specialMark;
    private String neutered;
    private String content;

    private String status;
    private List<AdoptionImage> path;
    private int hit;

    private Long postId;

    public AdoptionReadDto(Adoption adoption, Long postId){
        this.author=adoption.getMember().getName();
        this.title = adoption.getTitle();
        this.breed = adoption.getBreed();
        this.gender = adoption.getGender();
        this.age = adoption.getAge();
        this.center = adoption.getCenter();
        this.neutered = adoption.getNeutered();
        this.content = adoption.getContent();
        this.color= adoption.getColor();
        this.happenPlace = adoption.getHappenPlace();
        this.status= adoption.getStatus();
        this.specialMark=adoption.getSpecialMark();
        this.path=adoption.getAdoptionImages();

        this.hit = adoption.getHit();
        this.postId=postId;
    }
}
