package com.project.animal.adoption.dto;


import com.project.animal.adoption.domain.Adoption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdoptionReadDto {

    private String title;
    private String breed;
    private String gender;
    private int age;
    private String center;
    private String neutered;
    private String content;
    private String path;
    private int hit;

    public AdoptionReadDto(Adoption adoption){
        this.title = adoption.getTitle();
        this.breed = adoption.getBreed();
        this.gender = adoption.getGender();
        this.age = adoption.getAge();
        this.center = adoption.getCenter();
        this.neutered = adoption.getNeutered();
        this.content = adoption.getContent();
        this.path = adoption.getAdoptionImage().getPath();
        this.hit = adoption.getHit();
    }
}
