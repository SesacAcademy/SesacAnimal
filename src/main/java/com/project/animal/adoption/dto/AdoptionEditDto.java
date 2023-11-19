package com.project.animal.adoption.dto;


import com.project.animal.adoption.domain.Adoption;
import com.project.animal.adoption.domain.AdoptionImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdoptionEditDto {

    private Long authorId;

    private String author;

    private String title;

    private List<AdoptionImage> path;
//    private List<MultipartFile> adoptionImages;

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

    private int isActive;

    private Long postId;

    private String deleteImageId;


    public AdoptionEditDto(Adoption adoption, Long postId){
        this.authorId = adoption.getMember().getId();
        this.author = adoption.getMember().getNickname();
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
        this.isActive=1;
        this.postId=postId;
    }

    public AdoptionEditDto(Adoption adoption){
        this.author = adoption.getMember().getNickname();
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
        this.isActive=1;
    }

}
